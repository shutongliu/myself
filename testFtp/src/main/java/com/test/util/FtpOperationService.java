package com.test.util;

import com.jcraft.jsch.ChannelSftp;

import java.io.File;

/**
 * Created by liust on 2017/3/24.
 */
public interface FtpOperationService {
    void operation(ChannelSftp sftp, File file);
}
