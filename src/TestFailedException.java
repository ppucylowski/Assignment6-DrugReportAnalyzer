public class TestFailedException extends Exception {
    public Sample sample;

    public TestFailedException(String message, Sample sample) {
        super(message);
        this.sample = sample;
    }

    public Sample getSample() {
        return sample;
    }




}
