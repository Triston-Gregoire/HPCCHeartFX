package org.openjfx;

import org.hpccsystems.clienttools.EclCompile;
import org.hpccsystems.ws.client.HPCCWsSMCClient;
import org.hpccsystems.ws.client.platform.Platform;
import org.hpccsystems.ws.client.platform.Version;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Compiler {
    public static HashMap<String, String> compile(String ecl, String compilerDirectory, String tempDirectory,
                                                  List<String> additionalDirectories, List<String> flags, boolean saveTempFiles) throws Exception {
        return EclCompile.compileECL(ecl, compilerDirectory, tempDirectory, additionalDirectories, flags, saveTempFiles);
    }

//    public static void main(String[] args) {
//        Platform platform = Platform.get("http", "192.168.56.101", 8010, "hpccdemo", "hpccdemo");
//
//        HPCCWsSMCClient wssmc = null;
//        try {
//            wssmc = platform.getWsSMCClient();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Version targetVersion = new Version(wssmc.getHPCCBuild());
//
//        Version v = platform.getVersion();
//        System.out.println(v.toString());
//        String ecl = "//EXPORT movies := 'todo';\n" +
//                "import LearningTrees as LT;\n" +
//                "import ML_Core;\n" +
//                "import ML_Core.Discretize;\n" +
//                "layout := record\n" +
//                "\t\tunsigned ID;\n" +
//                "\t\tunsigned Genre1;\n" +
//                "\t\tunsigned Genre2;\n" +
//                "\t\tunsigned Genre3;\n" +
//                "    unsigned Director;\n" +
//                "    unsigned Actor_1;\n" +
//                "\t\tunsigned Actor_2;\n" +
//                "\t\tunsigned Actor_3;\n" +
//                "\t\tunsigned Actor_4;\n" +
//                "    unsigned Year;\n" +
//                "    unsigned Runtime;\n" +
//                "    unsigned Rating;\n" +
//                "    unsigned Votes;\n" +
//                "    unsigned Revenue;\n" +
//                "    unsigned Metascore;\n" +
//                "\t\tend;\n" +
//                "\t\t\n" +
//                "\n" +
//                "movies := pieChart('~online::tcg::transformedmovies.csv', layout , csv(separator(',')));\n" +
//                "\n" +
//                "reducedLayout := record\n" +
//                "\tunsigned ID := movies.id;\n" +
//                "\tunsigned Genre1 := movies.Genre1;\n" +
//                "\tunsigned Genre2 := movies.Genre2;\n" +
//                "\tunsigned Genre3 := movies.Genre3;\n" +
//                "\tunsigned Director := movies.Director;\n" +
//                "\tunsigned Actor_1 := movies.Actor_1;\n" +
//                "\tunsigned Actor_2 := movies.Actor_2;\n" +
//                "\tunsigned Actor_3 := movies.Actor_3;\n" +
//                "\tunsigned Actor_4 := movies.Actor_4;\n" +
//                "\tunsigned Year := movies.Year;\n" +
//                "\tunsigned Runtime := movies.runtime;\n" +
//                "\tunsigned Rating := movies.rating;\n" +
//                "\tunsigned Votes := movies.votes;\n" +
//                "\tunsigned Revenue := movies.revenue;\n" +
//                "\t//unsigned Metascore := movies.metascore;\n" +
//                "end;\n" +
//                "\n" +
//                "placeHolder := table(movies[2..], reducedLayout);\n" +
//                "testSet := placeHolder(Revenue != 999);\n" +
//                "//testSet := table(movies[2..], reducedLayout);\n" +
//                "//testSet := movies;\n" +
//                "testFormat := record(reducedLayout)\n" +
//                "\tunsigned4 rnd;\n" +
//                "end;\n" +
//                "\n" +
//                "nominal := [1,2,3,4,5,6,7,8];\n" +
//                "\n" +
//                "myData := PROJECT(testSet, TRANSFORM(testFormat, SELF.rnd := RANDOM() , SELF := LEFT));\n" +
//                "shuffledData := sort(myData, rnd);\n" +
//                "\n" +
//                "trainData := project(shuffledData[1..697], reducedLayout);\n" +
//                "testData := project(shuffledData[698..], reducedLayout);\n" +
//                "\n" +
//                "ML_Core.ToField(trainData, trainingDataNF);\n" +
//                "ML_Core.ToField(testData, testDataNF);\n" +
//                "testDataNF;\n" +
//                "indTestData := testDataNF(number < 13);\n" +
//                "depTestData := project(testDataNF(number = 13), transform(recordof(left), self.number := 1, self := left));\n" +
//                "\n" +
//                "indTrainData := trainingDataNF(number < 13);\n" +
//                "depTrainData := project(trainingDataNF(number = 13), transform(recordof(left), self.number := 1, self := left));\n" +
//                "\n" +
//                "depTrainDataDF := Discretize.ByRounding(depTrainData);\n" +
//                "depTestDataDF := Discretize.Byrounding(depTestData);\n" +
//                "\n" +
//                "learner := LT.ClassificationForest();\n" +
//                "modelC := learner.GetModel(indTrainData, depTrainDataDF);\n" +
//                "predictedClasses := learner.Classify(modelC, indTestData);\n" +
//                "assesmentC := ML_Core.Analysis.Classification.Accuracy(predictedClasses, depTestDataDF);\n" +
//                "assesmentC;\n" +
//                "\n" +
//                "lol := learner.FeatureImportance(modelC);\n" +
//                "lol;\n" +
//                "confusion := learner.ConfusionMatrix(modelC, depTestDataDF, indTestData);\n" +
//                "confusion;\n" +
//                "//assesment2 := ML_Core.Analysis.Classification.ConfusionMatrix(predictedClasses, depTestDataDF);\n" +
//                "//assesment2;\n" +
//                "\n" +
//                "//TRY INCREASING THE RANGE OF THE REVENUE BUCKETS\n";
//        String compiler = "C:\\Program Files\\HPCCSystems\\7.0.6\\clienttools\\bin";
//        String tempDirectory = "C:\\Users\\h3hmm\\Desktop\\ECL_compiler_temp";
//        List<String> aditionalDitectories = new ArrayList<>();
//        aditionalDitectories.add("C:\\Users\\Public\\Documents\\HPCC Systems\\ECL\\My Files\\LearningTrees");
//        aditionalDitectories.add("C:\\Users\\Public\\Documents\\HPCC Systems\\ECL\\My Files\\ML_Core");
//        List<String> flags = new ArrayList<>();
//
//        try {
//            HashMap<String, String> result = compile(ecl, compiler, tempDirectory, aditionalDitectories, flags, false);
//            System.out.print(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
