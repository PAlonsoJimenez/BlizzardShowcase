package demo.weblayer.restModel;

public class UpdateBeerRequest {
    private Beer beer;

    private UpdateBeerRequest() {
        // For Jackson
    }

    public UpdateBeerRequest(Beer beer) {
        this.beer = beer;
    }

    public Beer getBeer() {
        return beer;
    }
}
