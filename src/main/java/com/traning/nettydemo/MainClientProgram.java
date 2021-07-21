package com.traning.nettydemo;

import com.traning.nettydemo.config.ClientRunner;
import com.traning.nettydemo.config.ServerRunner;
import com.traning.nettydemo.handler.EchoServerHandler;
import com.traning.nettydemo.handler.client.EchoClientHandler;

/**
 * @Author Wong Jwei
 * @Date 2021/7/20
 * @Description
 */
public class MainClientProgram {

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new ClientRunner("localhost", port, new EchoClientHandler()).run();
    }

}
