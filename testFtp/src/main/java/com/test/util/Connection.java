package com.test.util;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;

/**
 * Created by liust on 2017/3/24.
 */
public class Connection {
    public static FtpOperationService ftpOperationService;
    public static ChannelSftp getChannel(Session session, String sPath, String dPath) {

        Channel channel = null;
        ChannelSftp sftp = null;
        try {
            channel = (Channel) session.openChannel("sftp");
            channel.connect(10000000);
            sftp = (ChannelSftp) channel;
            try {
                sftp.cd(dPath);
            } catch (SftpException e) {

                sftp.mkdir(dPath);
                sftp.cd(dPath);

            }
            File file = new File(sPath);
            ftpOperationService.operation(sftp, file);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
        return sftp;
    }
}
