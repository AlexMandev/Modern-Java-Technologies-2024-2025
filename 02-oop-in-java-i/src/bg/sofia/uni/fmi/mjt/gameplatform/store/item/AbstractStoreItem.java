package bg.sofia.uni.fmi.mjt.gameplatform.store.item;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class AbstractStoreItem implements StoreItem {
    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private double rating;
    private int ratingsCount;

    public AbstractStoreItem(String title, BigDecimal price, LocalDateTime releaseDate) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.ratingsCount = 0;
        this.rating = 0.0;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public BigDecimal getPrice() {
        return this.price;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return this.releaseDate;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }
    @Override
    public double getRating() {
        return this.rating;
    }

    @Override
    public void rate(double rating) {
        this.rating = (this.rating * this.ratingsCount  + rating) / (ratingsCount + 1);
        ratingsCount++;
    }
}
