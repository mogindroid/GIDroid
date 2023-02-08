package gin;

import com.opencsv.CSVWriter;
import gin.edit.Edit;
import gin.edit.statement.*;
import gin.test.AndroidTestRunner;
import gin.test.AndroidUnitTestResultSet;
import gin.util.AndroidConfig;
import gin.util.AndroidConfigReader;
import gin.util.AndroidProject;
import gin.util.NSGA.NSGAII;
import gin.util.NSGA.NSGAIII;
import gin.util.NSGA.SPEA2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.rng.simple.JDKRandomBridge;
import org.apache.commons.rng.simple.RandomSource;
import org.pmw.tinylog.Logger;
import org.zeroturnaround.exec.ProcessExecutor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class AndroidGI {
    AndroidTestRunner testRunner;
    SourceFile sourceFile;
    List<Class<? extends Edit>> editTypes;
    JDKRandomBridge rng;
    int indNumber = 50;
    int genNumber = 20;
    int NO_RUNS = 5;
    private final double tournamentPercentage = 0.2;
    private final double mutateProbability = 0.5;
    private final File outputFile;
    AndroidProject project;
    CSVWriter outputFileWriter;
    Map<String, AndroidUnitTestResultSet> book;

    public AndroidGI(AndroidTestRunner testRunner, SourceFileTree sourceFile, List<Class<? extends Edit>> editTypes, AndroidProject project) {
        long seed = Instant.now().getEpochSecond();
        this.testRunner = testRunner;
        this.sourceFile = sourceFile;
        this.editTypes = editTypes;
        this.rng = new JDKRandomBridge(RandomSource.MT, seed);
        outputFile = new File("log.txt");
        this.project = project;

    }

    public static void main(String[] args) {

        AndroidConfigReader configReader = new AndroidConfigReader("config.properties");
        AndroidConfig config = configReader.readConfig();
        String fileName = config.getFilePath();
        AndroidProject androidProject = new AndroidProject(config);
        AndroidTestRunner testRunner = new AndroidTestRunner(androidProject, config);
        List<Class<? extends Edit>> editTypes = Edit.parseEditClassesFromString(Edit.EditType.STATEMENT.toString());
        List<String> targetMethod = config.getTargetMethods();
        SourceFileTree sourceFile = (SourceFileTree) SourceFile.makeSourceFileForEditTypes(editTypes, fileName, targetMethod, androidProject.getClasspath());



        //// Local Search
        //AndroidGI androidGI = new AndroidGI(testRunner, sourceFile, editTypes, androidProject);
        //androidGI.localSearch();
        //// MO ALgorithms
        //NSGAII nsgaii = new NSGAII(androidProject, sourceFile, testRunner, editTypes);
        //nsgaii.run();
        //NSGAIII nsgaiii = new NSGAIII(androidProject, sourceFile, testRunner, editTypes);
        //nsgaiii.run();
//        SPEA2 spea2 = new SPEA2(androidProject, sourceFile, testRunner, editTypes);
//        spea2.run();

        System.exit(0);
    }


    public void localSearch() {
        Map<String, AndroidUnitTestResultSet> tested = new HashMap();
        writeHeader();
        Patch patch = new Patch(sourceFile);
        AndroidUnitTestResultSet origRes = testPatchLocally(patch, true, 2);
        AndroidUnitTestResultSet bestresults = null;
        double originalTime = origRes.getMemoryUsage();
        if (!origRes.isPatchValid()) {
            System.out.println("Tests failed on original app");
            System.exit(1);
        }
        Patch bestPatch = new Patch(this.sourceFile);
        writePatch(bestPatch, origRes, -1);
        tested.put(bestPatch.toString(), origRes);

        double bestTime = originalTime;
        for (int step = 0; step < 400; step++) {

            try {
                if (step % 5 == 0) {
                    project.killDaemon();
                }
                AndroidUnitTestResultSet results;

                Patch neighbour = neighbour(bestPatch);
                if (!tested.containsKey(neighbour.toString())) {
                    results = testPatchLocally(neighbour, true, 2);

                } else {
                    results = tested.get(neighbour.toString());
                }
                writePatch(neighbour, results, step);
                if (!results.isPatchValid()) {

                } else if (results.getMemoryUsage() < bestTime) {
                    bestPatch = neighbour;
                    bestTime = results.getMemoryUsage();
                    bestresults = results;
                }
            } catch (Exception e) {
                bestPatch.undoWrite(sourceFile.getFilename());
                e.printStackTrace();
                System.exit(1);
            }

        }
        System.out.println(bestPatch);
        System.out.println(originalTime);
        System.out.println(bestTime);
        if (bestresults == null) {
            bestresults = origRes;
        }
        writePatch(bestPatch, bestresults, -2);

    }


    Patch neighbour(Patch patch) {

        Patch neighbour = patch.clone();
        neighbour.addRandomEditOfClasses(rng, editTypes);
        return neighbour;

    }



    private AndroidUnitTestResultSet testPatch(Patch patch, boolean breakOnFail, int runs) {
        AndroidUnitTestResultSet result = new AndroidUnitTestResultSet(patch, false, new ArrayList<>());
        try {
            result = testRunner.runTests(patch, runs);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }


    private AndroidUnitTestResultSet testPatchLocally(Patch patch, boolean breakOnFail, int runs) {
        AndroidUnitTestResultSet result = new AndroidUnitTestResultSet(patch, false, new ArrayList<>());
        try {
            result = testRunner.runTestsLocally(patch, runs, breakOnFail);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return result;
    }

    private AndroidUnitTestResultSet testPatch(Patch patch) {
        return testPatch(patch, true, 5);
    }

    public void writeHeader() {
        String entry = "Gen, Ind, Patch, Valid, Fitness, Time\n";
        try {
            FileWriter writer = new FileWriter(outputFile, true);
            writer.write(entry);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }


    public void writePatch(Patch patch, AndroidUnitTestResultSet resultSet, int gen) {
        ZonedDateTime now = ZonedDateTime.now();
        now = now.withZoneSameInstant(ZoneId.of("UTC"));
        String entry =
                gen + ", " + patch.toString() + ", " +
                        resultSet.isPatchValid() + ", " +
                        resultSet.getMemoryUsage() + ", " + now + "\n";
        try {
            FileWriter writer = new FileWriter(outputFile, true);
            writer.write(entry);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void getDiff(String patch, int i) {
        Patch origPatch = new Patch(sourceFile);
        String origString = origPatch.apply();
        Patch newPatch = getPatch(patch);
        String newString = newPatch.apply();
        try {
            FileUtils.writeStringToFile(new File("source.original"), origString, Charset.defaultCharset());
        } catch (IOException e) {
            Logger.error("Could not write original source.");
            Logger.trace(e);
            System.exit(-1);
        }


        try {
            FileUtils.writeStringToFile(new File("source.patched"), newString, Charset.defaultCharset());
        } catch (IOException ex) {
            Logger.error("Could not write patched source.");
            Logger.trace(ex);
            System.exit(-1);
        }

        try {
            String output = new ProcessExecutor().command("diff", "source.original", "source.patched", "--side-by-side")
                    .readOutput(true).execute()
                    .outputUTF8();
            String filename = "diffs/" + project.getProjectName() + i;
            try {
                FileUtils.writeStringToFile(new File(filename), patch, Charset.defaultCharset());
                FileUtils.writeStringToFile(new File(filename), output, Charset.defaultCharset());
            } catch (IOException e) {
                Logger.error("Could not write original source.");
                Logger.trace(e);
                System.exit(-1);
            }
        } catch (IOException ex) {
            Logger.trace(ex);
            System.exit(-1);
        } catch (InterruptedException ex) {
            Logger.trace(ex);
            System.exit(-1);
        } catch (TimeoutException ex) {
            Logger.trace(ex);
            System.exit(-1);
        }


    }

    private Patch getPatch(String patch) {
        Patch origPatch = new Patch(sourceFile);
        if (patch.replaceAll("\\s+", "").equals("|")) {
            return origPatch;
        }
        String[] edits = patch.split("\\|");
        for (String edit : edits) {
            if (edit.equals("")) {
                continue;
            }
            String[] editTokens = edit.split(" ");
            String cls = editTokens[1];
            edit = edit.substring(1);
            switch (cls) {
                case "gin.edit.statement.CopyStatement":
                    origPatch.add(CopyStatement.fromString(edit));
                    break;
                case "gin.edit.statement.DeleteStatement":
                    origPatch.add(DeleteStatement.fromString(edit));
                    break;
                case "gin.edit.statement.ReplaceStatement":
                    origPatch.add(ReplaceStatement.fromString(edit));
                    break;
                case "gin.edit.statement.MoveStatement":
                    origPatch.add(MoveStatement.fromString(edit));
                    break;
                case "gin.edit.statement.SwapStatement":
                    origPatch.add(SwapStatement.fromString(edit));
                    break;
                case "gin.edit.statement.CacheEdit":
                    origPatch.add(CacheEdit.fromString(edit));
                    break;
            }

        }
        return origPatch;
    }


}
