package org.openjfx;

import org.hpccsystems.clienttools.EclCompile;
import org.hpccsystems.ws.client.HPCCWsSMCClient;
import org.hpccsystems.ws.client.platform.Platform;
import org.hpccsystems.ws.client.platform.Version;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
Compiler houses simple method to compile ecl locally for testing purposes
No longer needed
 */
public class Compiler {
    /*
    uses HPCC's client tools to compule ecl locally.
     */
    public static HashMap<String, String> compile(String ecl, String compilerDirectory, String tempDirectory,
                                                  List<String> additionalDirectories, List<String> flags, boolean saveTempFiles) throws Exception {
        return EclCompile.compileECL(ecl, compilerDirectory, tempDirectory, additionalDirectories, flags, saveTempFiles);
    }
}