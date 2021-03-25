/*Student: Corinne Bond, CS 220, Class ID: #02
* Student: Gavin Worley, CS 220, Class ID: #21
* Student: Shaun Wrenn, CS 220, Class ID: #22
* Assignemt: Project #01
* Description: This program converts decimal and fractional numbers to twos compliment, hexadecimal, and back to decimal. 
* Methods for the 4 basic arithmetic functions are also included to perform mathematical binary operations 
*/

public class BinaryNumber
{
// Do not change the instance variables or fields definition
// To simplify the project, we consider 8-bit input integers for arithmetic operations, except 4-bit integers for multiplication.
// intDigits[0] stores the MSB while intDigits[7] stores the LSB
	protected int[] intDigits = new int[8]; // to keep binary digits for integer component
	protected int[] fraDigits = new int[8]; // to keep binary digits for fractional component
	private int integer;

	// Constructors
	public BinaryNumber()
	{
		integer = 0;
		clearIntDigits(intDigits);
		convert(integer);
	}

	public BinaryNumber(int d)
	{
		integer = d;
		clearIntDigits(intDigits);
		clearFraDigits(fraDigits);
		convert(integer);
	}

	// converts a decimal number to binary two's compliment
	public void convert(int d)
	{
		// checking input
		if (d >= 256 || d <= -256)
		{
			throw new IllegalArgumentException("Number is out of range.");
		}
		// arithmetic to convert decimal to binary
		int index = 0;
		int temp = d; // holds original value for d
		if (d < 0) // to input into array as positive
		{
			d *= -1;
		}
		while (d > 0)
		{
			intDigits[index] = d % 2;
			d = d / 2;
			index++;
		}
		// converting negative numbers to twos compliment
		d = temp;
		if (d < 0)
		{
			negate(intDigits);
		}
	}

	// converts a fractional to binary two's compliment
	public void convert(double f)
	{
		// checking input
		if (f >= 256 || f <= -256)
		{
			throw new IllegalArgumentException("Number is out of range.");
		}
		// arithmetic to convert fractional to binary
		if (f < 0) // to input into array as positive
		{
			throw new IllegalArgumentException("Negative values are not accepted.");
		}
		int fInt = (int) f;
		if (fInt > 0)
		{
			clearIntDigits(intDigits);
		}
		double fractional = f % 1;
		convert(fInt);
		for (int x = 0; x < fraDigits.length; x++)
		{
			double result = (double) fractional * 2;
			fraDigits[x] = (int) result;
			fractional = result % 1;
			if (fraDigits[x] == -1)
			{
				fraDigits[x] *= -1;
			}
		}
	}

	// method to convert binary two's compliment to proper negative representation
	public void negate(int[] arr)
	{
		if (toDecimal(arr) > 0)
		{
			arr[arr.length - 1] = 1; // changing MSB sign bit
			// inverting the rest
			for (int i = 0; i < arr.length - 1; i++)
			{
				if (arr[i] == 1)
				{
					arr[i] = 0;
				} else
				{
					arr[i] = 1;
				}
			}
			arr[0] += 1;
			int carry = 0;
			if (arr[0] == 2)
			{
				arr[0] = 0;
				carry = 1;
			}
			while (carry > 0)
			{
				for (int x = 1; x < arr.length - 1; x++)
				{
					arr[x] += carry;
					if (arr[x] == 2)
					{
						arr[x] = 0;
						carry = 1;
					} else
					{
						carry = 0;
					}
				}
			}
		}
	}

	// method to add two decimal integers
	public BinaryNumber add(BinaryNumber BN)
	{
		int addDec = BN.integer;
		int dec = toDecimal(intDigits);
		int result = dec + addDec;
		clearIntDigits(intDigits); // making sure intDigits is empty
		convert(result);

		BinaryNumber temp = BN;
		temp.integer = BN.toDecimal(intDigits);
		return temp;
	}

	// method to subtract two decimal integers
	public BinaryNumber subtract(BinaryNumber BN)
	{
		int subDec = BN.integer;
		int dec = toDecimal(intDigits);
		int result = dec - subDec;
		clearIntDigits(intDigits); // making sure intDigits is empty
		convert(result);

		BinaryNumber temp = BN;
		temp.integer = BN.toDecimal(intDigits);
		return temp;
	}

	// method to multiply two decimal integers
	public BinaryNumber multiply(BinaryNumber BN)
	{
		int multDec = BN.integer;
		if (multDec >= 16 || multDec <= -16)
		{
			throw new IllegalArgumentException("Input exceeds 4 bits");
		}
		int dec = toDecimal(intDigits);
		if (dec >= 16 || dec <= -16)
		{
			throw new IllegalArgumentException("Input exceeds 4 bits");
		}
		int result = dec * multDec;
		clearIntDigits(intDigits); // making sure intDigits is empty
		convert(result);

		BinaryNumber temp = BN;
		temp.integer = BN.toDecimal(intDigits);
		return temp;
	}

	// method to divide two decimal integers
	public BinaryNumber divide(BinaryNumber BN)
	{
		int divDec = BN.integer;
		if (divDec < 0 || (divDec >= 256 || divDec <= -256))
		{
			throw new IllegalArgumentException("Negatives are not allowed for division");
		}
		int dec = toDecimal(intDigits);
		if (dec < 0)
		{
			throw new IllegalArgumentException("Negatives are not allowed for division");
		} else if (dec == 0 || (divDec >= 256 || divDec <= -256))
		{
			throw new IllegalArgumentException("Divide by zero error");
		}
		int result = dec / divDec;
		clearIntDigits(intDigits);
		convert(result);

		BinaryNumber temp = BN;
		temp.integer = BN.toDecimal(intDigits);
		return temp;
	}

	// converts a two's compliment array to a decimal numbers
	public int toDecimal(int[] binArr)
	{
		int currPow = 0;
		integer = 0; // clearing from overload constructor
		for (int x = 0; x < binArr.length; x++)
		{
			if (binArr[x] == 1)
			{
				integer += (int) Math.pow(2, currPow);
			}
			currPow++;
		}
		return integer;
	}

	// converts a two's compliment array to hexadecimal format
	public String toHex(int[] binArr)
	{
		int decimal = toDecimal(binArr);
		int rem = 0;
		char hex[] =
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		String hexNumStr = "";

		while (decimal > 0)
		{
			rem = decimal % 16;
			hexNumStr = hex[rem] + hexNumStr;
			decimal = decimal / 16;
		}
		// returning the final hex string
		return hexNumStr;
	}

	// helper method to clear intDigits array
	private void clearIntDigits(int[] arr)
	{
		for (int i = 0; i < arr.length; i++)
		{
			intDigits[i] = 0;
		}
	}

	// helper method to clear fraDigits
	private void clearFraDigits(int[] arr)
	{
		for (int i = 0; i < arr.length; i++)
		{
			fraDigits[i] = 0;
		}
	}

	// helper method for testing class
	protected int getInt()
	{
		return integer;
	}
	
	private int[] reverseArr(int[] arr)
	{
		int[] temp = new int[8];
		int index = 8;
		for (int i = 0; i < 8; i++)
		{
			temp[index-1] = arr[i];
			index--;
		}
		return temp;
	}

	// prints out formatted two's compliment array representations
	public String toString()
	{
		String phrase = "intDigits: [";
		for (int i = 7; i >= 0; i--)
		{
			if (i == 0)
			{
				phrase += intDigits[i] + "]";
			} else
			{
				phrase += intDigits[i] + ", ";
			}
		}
		phrase += "\nfraDigits: [";
		for (int i = 0; i < 8; i++)
		{
			if (i == 7)
			{
				phrase += fraDigits[i] + "]";
			} else
			{
				phrase += fraDigits[i] + ", ";
			}
		}

		return phrase + "\n";
	}
}