public class ComplexNumber {
    public int realPart;
    public int imagPart;
    public double magnitude;

    /**
     * Checks if the magnitude of a complex number is unique, namely, that there is only 1 a, b that satisfies a^2+b^2 = magnitudeSquared
     * @param maxAbsValue The upper range for (a,b)
     * @return The complex number has a unique magnitude
     */
    public boolean isUniqueMagnitude(int maxAbsValue) {
        double magnitudeSquared = this.getMagnitudeSquared();
        for (int i = 1; i <= maxAbsValue; i++) {
            for (int j = i + 1; j <= maxAbsValue; j++) { // Ensure j > i
                if ((i != realPart || j != imagPart) && (j != realPart || i != imagPart)) {
                    double otherMagnitudeSquared = i * i + j * j;
                    if (Math.abs(otherMagnitudeSquared - magnitudeSquared) < 1e-9) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void computeMagnitude() {
        this.magnitude = Math.sqrt(realPart*realPart + imagPart*imagPart);
    }

    public double getMagnitude() {
        return this.magnitude;
    }

    public double getMagnitudeSquared() {
        return this.getMagnitude()*this.getMagnitude();
    }

    public void setComplexNumber(int a, int b) {
        this.realPart = a;
        this.imagPart = b;
        computeMagnitude();
    }

    /**
     * Checks if complex numbers is equal
     * @param complexNumber ComplexNumber to be checked
     * @return Boolean of true or false
     */
    public boolean checkEqualComplex(ComplexNumber complexNumber) {
        return complexNumber.realPart == this.realPart && complexNumber.imagPart == this.imagPart;
    }

    /**
     * Will evaluate the differences between the real and imaginary parts of the complex numbers
     * @param complexNumber The ComplexNumber to be compared
     */
    public void compareComplex(ComplexNumber complexNumber) {
        if (complexNumber.realPart < this.realPart) {
            System.out.println("Real part too low");
        }
        else if (complexNumber.realPart > this.realPart) {
            System.out.println("Real part too high");
        }
        if (complexNumber.imagPart < imagPart) {
            System.out.println("Imaginary part too low");
        }
        else if (complexNumber.imagPart > imagPart) {
            System.out.println("Imaginary part too high");
        }
    }

    public String toString() {
        return "(" + this.realPart + " + " + this.imagPart + "i" + ")";
    }
}
