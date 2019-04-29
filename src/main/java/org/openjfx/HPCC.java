package org.openjfx;

import org.hpccsystems.ws.client.HPCCFileSprayClient;
import org.hpccsystems.ws.client.HPCCWsClient;
import org.hpccsystems.ws.client.HPCCWsFileIOClient;
import org.hpccsystems.ws.client.platform.Platform;
import org.hpccsystems.ws.client.platform.WorkunitInfo;
import org.hpccsystems.ws.client.utils.DelimitedDataOptions;
import org.junit.Assert;

/*
    HPCC is a wrapper class for the main HPCC Java API components used in this application.
 */
public class HPCC {
    private static Platform platform;
    private static HPCCWsClient client;
    private static DelimitedDataOptions options;
    private static WorkunitInfo workunitInfo;

    /*
    Uploads file at disk location pointed to by the path fileName to the HPCC landing zone and sprays it to the cluster

    @param String fileName - file path to upload and spray to HPCC cluster
    @returns boolean result - returns true if the spray is successful and false otherwise
     */
    static boolean spray(String fileName){
        try {
            HPCCWsFileIOClient wsFileIOClient = client.getWsFileIOClient();
            Assert.assertNotNull(wsFileIOClient);
            client.httpUploadFileToFirstHPCCLandingZone(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client.sprayVariableHPCCFile("predictme.csv", "~online::tcg::heart.csv", "hthor__myeclagent", options, true, HPCCFileSprayClient.SprayVariableFormat.DFUff_csv);
    }
    public static HPCCWsClient getClient() {
        return client;
    }

    public static void setClient(HPCCWsClient client) {
        HPCC.client = client;
    }

    public static Platform getPlatform() {
        return platform;
    }

    public static void setPlatform(Platform platform) {
        HPCC.platform = platform;
    }



    public static DelimitedDataOptions getOptions() {
        return options;
    }

    public static void setOptions(DelimitedDataOptions options) {
        HPCC.options = options;
    }

    public static WorkunitInfo getWorkunitInfo() {
        return workunitInfo;
    }

    public static void setWorkunitInfo(WorkunitInfo workunitInfo) {
        HPCC.workunitInfo = workunitInfo;
    }
}
