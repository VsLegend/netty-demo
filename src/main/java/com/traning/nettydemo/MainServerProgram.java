package com.traning.nettydemo;

import com.traning.nettydemo.config.ServerRunner;
import com.traning.nettydemo.handler.EchoServerHandler;

/**
 * @Author Wong Jwei
 * @Date 2021/7/20
 * @Description
 */
public class MainServerProgram {

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new ServerRunner(port, new EchoServerHandler()).run();
    }

}
