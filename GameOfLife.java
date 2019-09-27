import java.lang.reflect.InvocationTargetException;

public class GameOfLife {

	/**
	 * Die Main()-Methode.
	 */
	public static void main(final String[] args)
			throws InvocationTargetException, InterruptedException {
		/* Game of Life Simulation starten */
		start();
	}

	/**
	 * Initialisierung & Start der Simulation.
	 * 
	 * @throws InterruptedException
	 *             the interrupted exception
	 * @throws InvocationTargetException
	 */
	public static void start() {
		/* Erstellen der Matrizen */
		boolean[][] generationsMatrix = new boolean[60][60];

		/* Matrix initialisieren lassen */
		zufallsMatrixErstellen(generationsMatrix);
		GameOfLifeAnzeige.matrixInitialisieren(generationsMatrix);

		/* Simulation starten */
		simulation(generationsMatrix);
	}

	/**
	 * Erstellt eine Zufallsmatrix für das Game of Life
	 * 
	 * @param generationsMatrix
	 */
	public static void zufallsMatrixErstellen(boolean[][] generationsMatrix) {
		/* Zufallsmatrix mit Wahrscheinlichkeit 1/3 für lebende Zellen */
		for (int i = 0; i < generationsMatrix.length; i++) {
			for (int j = 0; j < generationsMatrix[i].length; j++) {
				if (Zufall.zufaelligenIntWertGenerieren(0, 2) == 2) {
					generationsMatrix[i][j] = true;
				}
			}
		}
	}

	/**
	 * Simulation der Generation.
	 * 
	 * @param generationsMatrix
	 *            die Generations-Matrix
	 * @throws InterruptedException
	 */
	public static void simulation(boolean[][] generationsMatrix) {
		/* Wer weiß, wer die Funktion sonst noch aufruft... */
		if (BoolscheMatrixFunktionen.istNichtLeereMatrix(generationsMatrix)) {
			/*
			 * Endlosschleife, da die Anzeige das Beenden des Programms für uns
			 * übernimmt.
			 */
			while (true) {
				/* Anzeige Aktualisieren mit 100ms Pause */
				GameOfLifeAnzeige.aktualisieren(generationsMatrix, 100);

				/* Berechnung der nächsten Generation durchführen */
				generationsMatrix = berechneNaechsteGeneration(generationsMatrix);
			}
		}
	}

	/**
	 * Berechnen der naechsten Generation.
	 * 
	 * @param generationsMatrix
	 *            die Generations-Matrix
	 * @return die neue Generations-Matrix
	 */
	public static boolean[][] berechneNaechsteGeneration(
			boolean[][] generationsMatrix) {
		boolean[][] naechsteGeneration = null;
		/* Wer weiß, wer die Funktion sonst noch aufruft... */
		if (BoolscheMatrixFunktionen.istNichtLeereMatrix(generationsMatrix)) {
			/*
			 * Eine leere Matrix der gleichen Größe für die naechste Generation
			 * erstellen.
			 */
			naechsteGeneration = new boolean[generationsMatrix.length][generationsMatrix[0].length];

			/* Die neue Generation nach den Regeln berechnen */
			for (int zeile = 0; zeile < naechsteGeneration.length; zeile += 1) {
				for (int spalte = 0; spalte < naechsteGeneration[zeile].length; spalte += 1) {
					/* Wende Regel an */
					regelAnwenden(generationsMatrix, naechsteGeneration, zeile,
							spalte);
				}
			}
		}
		/* Neue Generation zurückgeben */
		return naechsteGeneration;
	}

	/**
	 * Wende Generationswechsel-Regel an.
	 * 
	 * @param aktuelleGeneration
	 *            Die aktuelle Generation
	 * @param naechsteGeneration
	 *            Die naechste Generation
	 * @param zeile
	 *            Die Zeile der aktuellen Zelle
	 * @param spalte
	 *            Die Spalte der aktuellen Zelle
	 */
	public static void regelAnwenden(boolean[][] aktuelleGeneration,
			boolean[][] naechsteGeneration, int zeile, int spalte) {
		/* Wer weiß, wer die Funktion sonst noch aufruft... */
		if (BoolscheMatrixFunktionen.istGleicheNichtLeereDimension(
				aktuelleGeneration, naechsteGeneration)
				&& zeile >= 0
				&& zeile < aktuelleGeneration.length
				&& spalte >= 0
				&& spalte < aktuelleGeneration[0].length) {
			/* Hier die Regel zur Berechnung */
			int nachbarZellen = zaehleNachbarZellen(aktuelleGeneration, zeile,
					spalte);
			if (aktuelleGeneration[zeile][spalte]) {
				if (nachbarZellen == 2 || nachbarZellen == 3) {
					naechsteGeneration[zeile][spalte] = true;
				} else {
					naechsteGeneration[zeile][spalte] = false;
				}
			} else {
				if (nachbarZellen == 3) {
					naechsteGeneration[zeile][spalte] = true;
				} else {
					naechsteGeneration[zeile][spalte] = false;
				}
			}
		}
	}

	/**
	 * Zaehle aktive Nachbarzellen.
	 * 
	 * @param generation
	 *            Die Generation in der gezählt werden soll
	 * @param zeile
	 *            Die Zeile der aktuellen Zelle
	 * @param spalte
	 *            Die Spalte der aktuellen Zelle
	 * @return the Anzahl der aktiven Nachbarn
	 */
	public static int zaehleNachbarZellen(boolean[][] generation, int zeile,
			int spalte) {
		int nachbarZellen = 0;
		/* Wer weiß, wer die Funktion sonst noch aufruft... */
		if (BoolscheMatrixFunktionen.istNichtLeereMatrix(generation)
				&& zeile >= 0 && zeile < generation.length && spalte >= 0
				&& spalte < generation[0].length) {
			/* Zähle alle aktiven direkten Nachbarn der gegebenen Zelle. */

			/**
			 * Beispiel für eine innere Zelle: <br>
			 * <br>
			 * +---+---+---+ <br>
			 * | T | T | F | <br>
			 * +---+---+---+ <br>
			 * | F | X | F | <br>
			 * +---+---+---+ <br>
			 * | F | T | F | <br>
			 * +---+---+---+ <br>
			 * <br>
			 * => nachbarZellen = 3
			 */

			for (int i = zeile - 1; i <= zeile + 1; i += 1) {
				for (int j = spalte - 1; j <= spalte + 1; j += 1) {
					/*
					 * Prüfe ob die Indizes des Arrays gültig sind und schließe
					 * die gegebene Zelle von der Zählung aus.
					 */
					if (i >= 0 && i < generation.length && j >= 0
							&& j < generation[0].length
							&& !(i == zeile && j == spalte) && generation[i][j]) {
						nachbarZellen += 1;
					}
				}
			}
		}
		return nachbarZellen;
	}

}
