package de.results;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

public class CSVExporter<R extends RunResult> {
//    FileWriter csvWriter;

    private Path individualResults;
    private Path normalizedResults;
    private final String csvSuffix = ".csv";

    public CSVExporter(String optName) {
	Path tmpDir = FileSystems.getDefault().getPath("").toAbsolutePath();
	Path[] paths = { Paths.get("results"), Paths.get(optName), Paths.get("individual") };
	try {
	    for (int i = 0; i < paths.length; i++) {

		boolean resultFolderExists = fileExistsInDirectory(tmpDir, paths[i]);

		if (!resultFolderExists) {
		    if (i == 1) {
			normalizedResults = Files.createDirectories(tmpDir.resolve(paths[i]));
			tmpDir = normalizedResults;
		    } else if (i == 2) {
			individualResults = Files.createDirectories(tmpDir.resolve(paths[i]));
			tmpDir = individualResults;
		    } else {
			tmpDir = Files.createDirectories(tmpDir.resolve(paths[i]));
		    }

		} else {
		    if (i == 1) {
			normalizedResults = tmpDir.resolve(paths[i]);
			tmpDir = normalizedResults;
		    } else if (i == 2) {
			individualResults = tmpDir.resolve(paths[i]);
			tmpDir = individualResults;
		    } else {
			tmpDir = tmpDir.resolve(paths[i]);
		    }

		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    private boolean fileExistsInDirectory(Path directory, Path file) throws IOException {
	for (Path element : Files.newDirectoryStream(directory)) {
	    if (element.getFileName().equals(file)) {
		return true;
	    }
	}
	return false;
    }

    private Path createResultFile(String name) throws IOException {
	String fileName = new StringBuffer().append(name).append(csvSuffix).toString();
	Path resFile = individualResults.resolve(Paths.get(fileName));
	Files.deleteIfExists(resFile);
	return Files.createFile(resFile);

    }

    public void write(List<R> result, Scenario scenario, String run) {
	try {
	    Path resFile = createResultFile(
		    new StringBuffer().append(run).append("_").append(scenario.getResultFile()).toString());

	    Writer writer = new PrintWriter(resFile.toFile());
	    StatefulBeanToCsv<R> beanToCsv = new StatefulBeanToCsvBuilder<R>(writer).build();
	    try {
		for (R res : result) {
		    beanToCsv.write(res);
		}
	    } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    writer.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
