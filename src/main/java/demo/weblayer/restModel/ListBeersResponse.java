package demo.weblayer.restModel;

import java.util.List;

public class ListBeersResponse {
    private List<Beer> beerList;

    private ListBeersResponse() {
        // For Jackson
    }

    public ListBeersResponse(List<Beer> beerList) {
        this.beerList = beerList;
    }

    public List<Beer> getBeerList() {
        return beerList;
    }
}
