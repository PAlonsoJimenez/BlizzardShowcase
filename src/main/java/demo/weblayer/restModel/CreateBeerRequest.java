package demo.weblayer.restModel;

public class CreateBeerRequest {
    private Beer beer;

    private CreateBeerRequest() {
        // For Jackson
    }

    public CreateBeerRequest(Beer beer) {
        this.beer = beer;
    }

    public Beer getBeer() {
        return beer;
    }

}
