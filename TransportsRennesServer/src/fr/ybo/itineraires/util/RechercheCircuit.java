/*
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.ybo.itineraires.util;

import fr.ybo.gtfs.modele.Arret;
import fr.ybo.gtfs.modele.GestionnaireGtfs;
import fr.ybo.itineraires.modele.*;

import java.util.*;

public class RechercheCircuit {

	private final static double distanceRechercheArrets = 400;
	private final Adresse adresseDepart;
	private final Adresse adresseArrivee;
	private final List<JointurePieton> arretsDeparts = new ArrayList<JointurePieton>();
	private final List<JointurePieton> arretsArrivees = new ArrayList<JointurePieton>();

	public RechercheCircuit(Adresse adresseDepart, Adresse adresseArrivee) {
		super();
		this.adresseDepart = adresseDepart;
		this.adresseArrivee = adresseArrivee;
	}

	private final List<Trajet> bestTrajets = new ArrayList<Trajet>(3);

	public List<Chrono> calculCircuits(EnumCalendrier calendrier, int heureDepart) {
		List<Chrono> chronos = new ArrayList<Chrono>();
		Chrono chrono = new Chrono("ArretEligibles");
		for (Arret arret : GestionnaireGtfs.getInstance().getAllArrets()) {
			double distance = calculDistanceDepart(arret);
			if (distance < distanceRechercheArrets) {
				arretsDeparts.add(new JointurePieton(arret, adresseDepart, distance));
			}
			distance = calculDistanceArrivee(arret);
			if (distance < distanceRechercheArrets) {
				arretsArrivees.add(new JointurePieton(arret, adresseArrivee, distance));
			}
		}
		chronos.add(chrono.stop());
		chrono = new Chrono("RemplirCircuits");
		List<Circuit> circuits = new ArrayList<Circuit>();
		for (JointurePieton arretDepart : arretsDeparts) {
			for (JointurePieton arretArrivee : arretsArrivees) {
				circuits.add(new Circuit(arretDepart, arretArrivee));
			}
		}
		chronos.add(chrono.stop());
		chrono = new Chrono("RechercheTrajets");
		Iterator<Circuit> iteratorCircuit = circuits.iterator();
		while (iteratorCircuit.hasNext()) {
			if (iteratorCircuit.next().rechercheTrajetBus(calendrier, heureDepart)) {
				iteratorCircuit.remove();
			}
		}
		chronos.add(chrono.stop());
		chrono = new Chrono("RemplirTrajets");
		List<Trajet> trajets = new ArrayList<Trajet>();
		for (Circuit circuit : circuits) {
			trajets.addAll(circuit.getTrajets());
		}
		chronos.add(chrono.stop());
		chrono = new Chrono("SortTrajets");
		Collections.sort(trajets, new ComparatorTrajet(heureDepart));
		chronos.add(chrono.stop());
		bestTrajets.addAll(trajets.subList(0, trajets.size() > 3 ? 3 : trajets.size()));
		return chronos;
	}

	private class ComparatorTrajet implements Comparator<Trajet> {
		private final int heureDepart;

		private ComparatorTrajet(int heureDepart) {
			this.heureDepart = heureDepart;
		}

		public int compare(Trajet o1, Trajet o2) {
			int tempsTrajet1 = o1.calculTempsTrajet(heureDepart);
			int tempsTrajet2 = o2.calculTempsTrajet(heureDepart);
			return (tempsTrajet1 < tempsTrajet2 ? -1 : (tempsTrajet1 == tempsTrajet2 ? 0 : 1));
		}
	}

	public List<Trajet> getBestTrajets() {
		return bestTrajets;
	}

	private double calculDistanceDepart(Arret arret) {
		return calculDistance(adresseDepart.getLatitude(), adresseDepart.getLongitude(), arret.latitude, arret.longitude);
	}

	private double calculDistanceArrivee(Arret arret) {
		return calculDistance(adresseArrivee.getLatitude(), adresseArrivee.getLongitude(), arret.latitude, arret.longitude);
	}

	private static double calculDistance(double lat1, double lon1, double lat2, double lon2) {
		// Based on http://www.ngs.noaa.gov/PUBS_LIB/inverse.pdf
		// using the "Inverse Formula" (section 4)

		int MAXITERS = 20;
		// Convert lat/long to radians
		lat1 *= Math.PI / 180.0;
		lat2 *= Math.PI / 180.0;
		lon1 *= Math.PI / 180.0;
		lon2 *= Math.PI / 180.0;

		double a = 6378137.0; // WGS84 major axis
		double b = 6356752.3142; // WGS84 semi-major axis
		double f = (a - b) / a;
		double aSqMinusBSqOverBSq = (a * a - b * b) / (b * b);

		double L = lon2 - lon1;
		double A = 0.0;
		double U1 = Math.atan((1.0 - f) * Math.tan(lat1));
		double U2 = Math.atan((1.0 - f) * Math.tan(lat2));

		double cosU1 = Math.cos(U1);
		double cosU2 = Math.cos(U2);
		double sinU1 = Math.sin(U1);
		double sinU2 = Math.sin(U2);
		double cosU1cosU2 = cosU1 * cosU2;
		double sinU1sinU2 = sinU1 * sinU2;

		double sigma = 0.0;
		double deltaSigma = 0.0;
		double cosSqAlpha;
		double cos2SM;
		double cosSigma;
		double sinSigma;
		double cosLambda;
		double sinLambda;

		double lambda = L; // initial guess
		for (int iter = 0; iter < MAXITERS; iter++) {
			double lambdaOrig = lambda;
			cosLambda = Math.cos(lambda);
			sinLambda = Math.sin(lambda);
			double t1 = cosU2 * sinLambda;
			double t2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda;
			double sinSqSigma = t1 * t1 + t2 * t2; // (14)
			sinSigma = Math.sqrt(sinSqSigma);
			cosSigma = sinU1sinU2 + cosU1cosU2 * cosLambda; // (15)
			sigma = Math.atan2(sinSigma, cosSigma); // (16)
			double sinAlpha = (sinSigma == 0) ? 0.0 : cosU1cosU2 * sinLambda / sinSigma; // (17)
			cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
			cos2SM = (cosSqAlpha == 0) ? 0.0 : cosSigma - 2.0 * sinU1sinU2 / cosSqAlpha; // (18)

			double uSquared = cosSqAlpha * aSqMinusBSqOverBSq; // defn
			A = 1 + (uSquared / 16384.0) * // (3)
					(4096.0 + uSquared * (-768 + uSquared * (320.0 - 175.0 * uSquared)));
			double B = (uSquared / 1024.0) * // (4)
					(256.0 + uSquared * (-128.0 + uSquared * (74.0 - 47.0 * uSquared)));
			double C = (f / 16.0) * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha)); // (10)
			double cos2SMSq = cos2SM * cos2SM;
			deltaSigma = B * sinSigma * // (6)
					(cos2SM + (B / 4.0) *
							(cosSigma * (-1.0 + 2.0 * cos2SMSq) - (B / 6.0) * cos2SM * (-3.0 + 4.0 * sinSigma * sinSigma) * (-3.0 + 4.0 * cos2SMSq)));

			lambda = L + (1.0 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SM + C * cosSigma * (-1.0 + 2.0 * cos2SM * cos2SM))); // (11)

			double delta = (lambda - lambdaOrig) / lambda;
			if (Math.abs(delta) < 1.0e-12) {
				break;
			}
		}

		return (b * A * (sigma - deltaSigma));

	}

}