package com.mii.komi.controller;

import com.mii.komi.dto.sysmon.Sysmon;
import com.mii.komi.dto.sysmon.TCPServer;
import com.mii.komi.dto.sysmon.TCPServerConnection;
import com.mii.komi.dto.sysmon.TxnMgr;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jpos.iso.ISOChannel;
import org.jpos.iso.ISOServer;
import org.jpos.transaction.TransactionManager;
import org.jpos.util.NameRegistrar;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vinch
 */
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
            RequestMethod.GET, 
            RequestMethod.POST, 
            RequestMethod.OPTIONS,
            RequestMethod.PUT,
            RequestMethod.PATCH,
            RequestMethod.HEAD,
            RequestMethod.DELETE,
            RequestMethod.TRACE})
@Api(tags = {"System Monitor API"})
@RestController
@RequestMapping("/komi/api/v1/adapter/sysmon")
public class StatusMonitoringController {

    public static final String MODULE_TYPE_TCP_SERVER = "TCP-Server";
    public static final String MODULE_TYPE_TCP_CLIENT = "TCP-Client";
    public static final String MODULE_TYPE_TXNMGR = "Transaction-Manager";

    @ApiOperation(value = "System Monitor", nickname = "System Monitor API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @GetMapping(path = {"", "/"})
    public ResponseEntity<List<Sysmon>> getModuleNames() throws NameRegistrar.NotFoundException {
        Map<String, Object> map = NameRegistrar.getAsMap();
        List<Sysmon> sysmons = new ArrayList<>();
        Iterator<Map.Entry<String, Object>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> entry = itr.next();
            if (entry.getValue() instanceof ISOServer) {
                Sysmon sysmon = Sysmon.success(entry.getKey(), MODULE_TYPE_TCP_SERVER);
                sysmons.add(sysmon);
            } else if(entry.getValue() instanceof TransactionManager) {
                Sysmon sysmon = Sysmon.success(entry.getKey(), MODULE_TYPE_TXNMGR);
                sysmons.add(sysmon);
            }
        }
        return ResponseEntity.ok(sysmons);
    }
    
    @ApiOperation(value = "System Monitor Detail", nickname = "System Monitor Detail API")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully get data"),
        @ApiResponse(code = 401, message = "You're not authorized to access this endpoint"),
        @ApiResponse(code = 403, message = "Forbidden"),
        @ApiResponse(code = 404, message = "Page Not Found")
    })
    @GetMapping("/{moduleName}")
    public ResponseEntity<Sysmon> getModuleName(@PathVariable String moduleName) {
        Sysmon sysmon = null;
        try {
            Object o = NameRegistrar.get(moduleName);
            if(o instanceof ISOServer) {
                ISOServer server = (ISOServer)o;
                TCPServer tcpServer = new TCPServer();
                tcpServer.setActiveConnections(server.getActiveConnections());
                if (server.getLastTxnTimestampInMillis() > 0l) {
                    tcpServer.setLastTransactions(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                            .format(LocalDateTime.ofInstant(Instant.ofEpochMilli(server.getLastTxnTimestampInMillis()),
                                    ZoneId.systemDefault())));
                }
                if(server.getISOChannelNames().trim().length() > 0) {
                    String[] isoChannels = server.getISOChannelNames().trim().split(" ");
                    for(int i = 0; i < isoChannels.length; i++) {
                        TCPServerConnection conn = new TCPServerConnection();
                        conn.setAddress(isoChannels[i]);
                        ISOChannel isoc = server.getISOChannel(isoChannels[i]);
                        boolean connected = false;
                        if(isoc != null && isoc.isConnected()) {
                            connected = true;
                        }
                        conn.setIsConnected(connected);
                        tcpServer.getConnections().add(conn);
                    }
                }
                sysmon = Sysmon.success(moduleName, MODULE_TYPE_TCP_SERVER, tcpServer);
            } else if(o instanceof TransactionManager) {
                TransactionManager transactionManager = (TransactionManager) o;
                TxnMgr txnMgr = new TxnMgr();
                txnMgr.setTps(transactionManager.getTPSAsString());
                txnMgr.setTpsAverage(transactionManager.getTPSAvg());
                txnMgr.setTpsPeak(transactionManager.getTPSPeak());
                sysmon = Sysmon.success(moduleName, MODULE_TYPE_TXNMGR, txnMgr);
            }
            return ResponseEntity.ok(sysmon);
        } catch (NameRegistrar.NotFoundException ex) {
            Logger.getLogger(StatusMonitoringController.class.getName()).log(Level.SEVERE, null, ex);
            return ResponseEntity.notFound().build();
        }
    }

}
