package assignment6;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Yodle {

	public ArrayList<Circuit> circuits = new ArrayList<Circuit>();
	public List<Juggler> jugglers = new ArrayList<Juggler>();
	public int mJugglerNum;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Yodle myYodle = new Yodle();
		Yodle.Producer myProducer = myYodle.new Producer();
		/*
		 * execute the reading and sorting, followed by the display of the
		 * results
		 */
		myProducer.execute();
		myProducer.display(myYodle.circuits);
	}

	private class Juggler {
		public Juggler(int[] mSkills, String[] mPreferences, int mPass,
				String mNumber) {
			super();
			this.mSkills = mSkills;
			this.mPreferences = mPreferences;
			this.mPass = mPass;
			this.mNumber = mNumber;
		}

		/**
		 * The skills of the Juggler. Index zero representing H, one is E and
		 * two is P
		 **/
		private int[] mSkills;

		/** The circuit preferences of the Juggler in order of preference **/
		private String[] mPreferences;

		/**
		 * this is a marker or indicator on which preference level this juggler
		 * is now being considered for
		 **/
		private int mPass;

		/**
		 * this represents the current dot product value that this juggler is
		 * being considered for
		 **/
		private int mDotProduct;

		/** this is the code of the Juggler **/
		private String mNumber;

		/**
		 * @return the mSkills
		 */
		public int[] getmSkills() {
			return mSkills;
		}

		/**
		 * @param mSkills
		 *            the mSkills to set
		 */
		public void setmSkills(int[] mSkills) {
			this.mSkills = mSkills;
		}

		/**
		 * @return the mPreference at mIndex
		 */
		public String getmPreferences(int mIndex) {
			return mPreferences[mIndex];
		}

		/**
		 * @param mPreferences
		 *            the mPreferences to set
		 */
		public void setmPreferences(String[] mPreferences) {
			this.mPreferences = mPreferences;
		}

		/**
		 * @return the mDotProduct
		 */
		public int getmDotProduct() {
			return mDotProduct;
		}

		/**
		 * @param mDotProduct
		 *            the mDotProduct to set
		 */
		public void setmDotProduct(int mDotProduct) {
			this.mDotProduct = mDotProduct;
		}

		/**
		 * @return the mPass
		 */
		public int getmPass() {
			return mPass;
		}

		/**
		 * @param mPass
		 *            the mPass to set
		 */
		public void setmPass(int mPass) {
			this.mPass = mPass;
		}

		/**
		 * @return the mNumber
		 */
		public String getmNumber() {
			return mNumber;
		}

		/**
		 * @param mNumber
		 *            the mNumber to set
		 */
		public void setmNumber(String mNumber) {
			this.mNumber = mNumber;
		}
	}

	private class Circuit {
		/**
		 * The skills of the Circuit. Index zero representing H, one is E and
		 * two is P
		 **/
		private int[] mSkills;

		/** this is the code of the Circuit **/
		private String mNumber;

		/**
		 * This represents the index of the Juggler with the lowest match in the
		 * group
		 **/
		int mMinIndex;

		/** This respresents the list of Jugglers currently in the circuit **/
		List<Juggler> mCircuitList;

		/**
		 * @param mSkills
		 *            The skills of the Circuit. Index zero representing H, one
		 *            is E and two is P
		 * @param mNumber
		 *            this is the code of the Circuit
		 */
		public Circuit(int[] mSkills, String mNumber) {
			super();
			this.mSkills = mSkills;
			this.mNumber = mNumber;
			this.mCircuitList = new ArrayList<Juggler>();
		}

		/**
		 * @return the mSkills
		 */
		public int[] getmSkills() {
			return mSkills;
		}

		/**
		 * @return the mNumber
		 */
		public String getmNumber() {
			return mNumber;
		}

		/**
		 * Adds a Juggler to the circuit if there is space and return a null i.e
		 * number of jugglers less than the allowed maximum number for a
		 * circuit. If there is no space, it would locate the jugler with the
		 * minimum match and comapare. if the minimum juggler has a higher
		 * value, the juggler to be added would be sent back else the juggler to
		 * be added would replace the minimum-valued juggler and that juggler
		 * would be returned.
		 * 
		 * @param mJuggler
		 *            Juggler to be added
		 * @return Juggler that was kicked out of the circuit or the juggler who
		 *         was sent to be added if its match number for this circuit is
		 *         lower than its members
		 */
		public Juggler addJuggler(Juggler mJuggler) {

			if (mCircuitList.size() < mJugglerNum) {
				mCircuitList.add(mJuggler);
				updateMin();
				return null;
			} else {
				if (Compare(mJuggler, mCircuitList.get(mMinIndex)) == 1) {
					mCircuitList.add(mJuggler);
					mJuggler = mCircuitList.remove(mMinIndex);
					updateMin();
					return mJuggler;
				} else
					return mJuggler;
			}
		}

		/**
		 * Finds the current Juggler with the minimum match in the circuit and
		 * stores its position in the mMindex field
		 */
		private void updateMin() {
			if (!mCircuitList.isEmpty()) {
				mMinIndex = 0;
				int mMin = mCircuitList.get(mMinIndex).getmDotProduct();
				for (int i = 0; i < mCircuitList.size(); i++) {
					if (mMin > mCircuitList.get(i).getmDotProduct()) {
						mMin = mCircuitList.get(i).getmDotProduct();
						mMinIndex = i;
					}
				}
			}
		}

		/**
		 * Compares two Jugglers based on their dot product(match) values and
		 * then their preferences. 1 is returned if mJuggler1 has a higher match
		 * value than mJuggler2 else a -1 is returned if its vice versa. If the
		 * match values are the same, the second condition to be checked is
		 * their preference values. If mJuggler1 has a higher prefence
		 * value(indicated by a lower mPass level), a 1 would be returned else
		 * -1 if vice versa. 0 would be returned only if both Jugglers have the
		 * same match values and preference level.
		 * 
		 * @param mJuggler1
		 * @param mJuggler2
		 * @return
		 */
		public int Compare(Juggler mJuggler1, Juggler mJuggler2) {
			if (mJuggler1.getmDotProduct() > mJuggler2.getmDotProduct()) {
				return 1;
			} else if (mJuggler1.getmDotProduct() < mJuggler2.getmDotProduct()) {
				return -1;
			} else {
				if (mJuggler1.getmPass() < mJuggler2.getmPass()) {
					return 1;
				} else if (mJuggler1.getmPass() > mJuggler2.getmPass()) {
					return -1;
				} else {
					return 0;
				}
			}

		}

	}

	/**
	 * 
	 * Ignore funky class name. This class manages the whole operation, reading
	 * from file and sorting
	 * 
	 */
	private class Producer {
		BufferedReader mInput;
		String mLine;
		String[] mToken;

		/**
		 * Similar to the main method of this class. Reads data, controls other
		 * functioins to do the work
		 */
		public void execute() {
			try {
				mInput = new BufferedReader(new FileReader("jugglefest.txt"));
				int count = 1;
				while ((mLine = mInput.readLine()) != null) {
					mToken = mLine.split(" ");
					if (mToken.equals(null))
						System.err.println("Invalid Circuit or Juggler ");
					else {
						switch (mToken[0]) {
						case "C":
							circuits.add(new Circuit(new int[] {
									Integer.parseInt(mToken[2].substring(2)),
									Integer.parseInt(mToken[3].substring(2)),
									Integer.parseInt(mToken[4].substring(2)) },
									mToken[1]));
							break;
						case "J":
							count++;
							int[] skills = new int[] {
									Integer.parseInt(mToken[2].substring(2)),
									Integer.parseInt(mToken[3].substring(2)),
									Integer.parseInt(mToken[4].substring(2)) };
							jugglers.add(new Juggler(skills, mToken[5]
									.split(","), 0, mToken[1]));
							break;
						default:
							System.err
									.println("Invalid Circuit or Juggler code at line " + count);
							break;
						}
					}
				}

				mJugglerNum = jugglers.size() / circuits.size();
				Juggler aJuggler;
				while (!jugglers.isEmpty()) {
					aJuggler = jugglers.remove(0);
					int mDesiredCircuit = -1;
					/*
					 * Check if the juggler is out of options for circuit then
					 * assign to random circuit
					 */
					if (aJuggler.getmPass() >= aJuggler.mPreferences.length) {
						do {
							mDesiredCircuit++;
						} while (circuits.get(mDesiredCircuit).mCircuitList
								.size() >= mJugglerNum); // check to make sure
															// circuit is not
															// full before added
															// random juggler
					} else
						mDesiredCircuit = Integer.parseInt(aJuggler
								.getmPreferences(aJuggler.getmPass())
								.substring(1));
					aJuggler.setmDotProduct(dotProduct(aJuggler.getmSkills(),
							circuits.get(mDesiredCircuit).getmSkills()));
					aJuggler = circuits.get(mDesiredCircuit).addJuggler(
							aJuggler);
					if (aJuggler != null) {
						aJuggler.setmPass(1 + aJuggler.getmPass());
						jugglers.add(aJuggler);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("Input file could not be located");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("Input file could not be read");
			} finally {
				try {
					if (mInput != (null))
						mInput.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		/**
		 * Dot product calculator. Checks to make sure arrays are right length
		 * and not null
		 * 
		 * @param mJuggler
		 *            skill set of Juggler
		 * @param mCircuit
		 *            skill set of circuit
		 * @return dot product value
		 */
		private int dotProduct(int[] mJuggler, int[] mCircuit) {
			if ((mJuggler.length != mCircuit.length) || mJuggler.equals(null)
					|| mCircuit.equals(null)) {
				throw new IllegalArgumentException(
						"Invalid Juggler or Circuit ability array");
			}
			int mProduct = 0;
			for (int i = 0; i < mJuggler.length; i++) {
				mProduct += (mJuggler[i] * mCircuit[i]);
			}
			return mProduct;
		}

		/**
		 * Displays the content of a list of circuits. Will list the circuit
		 * number followed by the jugglers and their preferences each. Designed
		 * to write out to a text file called textout.txt
		 * 
		 * @param mCircuit
		 *            the list of circuits to be displayed.
		 */
		public void display(ArrayList<Circuit> mCircuit) {
			PrintWriter mPrintln;
			try {
				mPrintln = new PrintWriter(new BufferedWriter(new FileWriter(
						"textout.txt")));
				for (Circuit aCircuit : mCircuit) {
					mPrintln.print(aCircuit.getmNumber() + " ");
					for (Juggler aJuggler : aCircuit.mCircuitList) {
						mPrintln.print(aJuggler.getmNumber() + " ");
						for (String mCircuitString : aJuggler.mPreferences) {
							mPrintln.print(mCircuitString + ":");
							mPrintln.print(dotProduct(
									aJuggler.getmSkills(),
									mCircuit.get(
											Integer.parseInt(mCircuitString
													.substring(1)))
											.getmSkills()));
							mPrintln.print(" ");
						}
						mPrintln.print(", ");
					}
					mPrintln.println();
				}
				mPrintln.flush();
				mPrintln.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
