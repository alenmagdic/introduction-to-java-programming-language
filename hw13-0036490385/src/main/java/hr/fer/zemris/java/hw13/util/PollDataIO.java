package hr.fer.zemris.java.hw13.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.java.hw13.glasanje.Band;
import hr.fer.zemris.java.hw13.glasanje.PollResult;
import hr.fer.zemris.java.hw13.listeneri.ResourcePathsArchiver;

/**
 * Class that contains static methods for writing and reading poll data. It
 * contains a method for loading the poll data definition, for creating a file
 * with empty results (i.e. all votes are set to 0) and for loading the poll
 * results.
 *
 * @author Alen Magdić
 *
 */
public class PollDataIO {

	/**
	 * Loads the poll definition and returns it in a form of set of bands. Bands
	 * are the poll candidates.
	 *
	 * @return set of bands that are the poll candidates
	 * @throws IOException
	 *             if there is a problem reading from poll definition file
	 */
	public static Set<Band> loadVoteDataDefinition() throws IOException {
		List<String> bandData = Files.readAllLines(ResourcePathsArchiver.POLL_DEFINITION_FILE_PATH);
		Set<Band> bands = new TreeSet<>();

		for (String band : bandData) {
			String[] data = band.split("\t");
			if (data.length != 3) {
				continue;
			}

			int id;
			id = Integer.parseInt(data[0]);

			bands.add(new Band(id, data[1], data[2]));
		}
		return bands;

	}

	/**
	 * Creates a file containing empty results, i.e. results with all votes set
	 * to 0.
	 *
	 * @return list of lines of the generated file
	 * @throws IOException
	 *             if there is a problem writing to poll results file
	 */
	public static List<String> createFileWithEmptyResults() throws IOException {
		return createFileWithEmptyResults(loadVoteDataDefinition());
	}

	/**
	 * Creates a file containing empty results, i.e. results with all votes set
	 * to 0.
	 *
	 * @param bands
	 *            set of poll candidates, i.e. music bands
	 * @return list of lines of the generated file
	 * @throws IOException
	 *             if there is a problem writing to poll results file
	 */
	public static List<String> createFileWithEmptyResults(Set<Band> bands) throws IOException {
		List<String> lines = new ArrayList<>();
		BufferedWriter writer = Files.newBufferedWriter(ResourcePathsArchiver.POLL_RESULTS_FILE_PATH);

		for (Band band : bands) {
			lines.add(band.getId() + "\t0");
		}

		for (String line : lines) {
			writer.write(line + "\n");
		}

		writer.close();
		return lines;
	}

	/**
	 * Loads the poll results and models them like a set of {@link PollResult}
	 * objects.
	 *
	 * @return the poll results modeled in a form of set of {@link PollResult}
	 *         objects
	 * @throws IOException
	 *             if there is a problem reading the poll results file
	 */
	public static Set<PollResult> loadResults() throws IOException {
		Set<Band> bands = loadVoteDataDefinition();

		List<String> voteResults;
		if (!Files.exists(ResourcePathsArchiver.POLL_RESULTS_FILE_PATH)) {
			voteResults = createFileWithEmptyResults(bands);
		} else {
			voteResults = Files.readAllLines(ResourcePathsArchiver.POLL_RESULTS_FILE_PATH);
		}

		Map<Integer, Band> bandsMap = mapBandIdToBand(bands);

		Set<PollResult> results = new TreeSet<>((r1, r2) -> {
			int res = r2.getVotes() - r1.getVotes();
			if (res != 0) {
				return res;
			}
			res = r1.getBand().getName().compareTo(r2.getBand().getName());
			if (res != 0) {
				return res;
			}
			return r1.getBand().compareTo(r2.getBand());
		});

		for (String voteResult : voteResults) {
			String[] res = voteResult.split("\t");
			Band band = bandsMap.get(Integer.parseInt(res[0]));
			results.add(new PollResult(band, Integer.parseInt(res[1].trim())));
		}
		return results;

	}

	/**
	 * Creates a map that associates band ids to bands.
	 *
	 * @param bands
	 *            set of bands
	 * @return a map that associates band ids to bands
	 */
	private static Map<Integer, Band> mapBandIdToBand(Set<Band> bands) {
		Map<Integer, Band> bandsMap = new HashMap<>();
		for (Band band : bands) {
			bandsMap.put(band.getId(), band);
		}
		return bandsMap;
	}

}
