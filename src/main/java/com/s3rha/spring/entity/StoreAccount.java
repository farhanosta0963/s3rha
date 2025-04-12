package com.s3rha.spring.entity;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//2. Store Account Entity
////        java
//Copy
@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Setter
@Getter

public class StoreAccount extends Account {

    public StoreAccount() {
        setAccountType("STORE");
    }
    private String name;
    private Boolean verifiedFlag;

//    @OneToMany(mappedBy = "storeAccount",
//            fetch = FetchType.EAGER,
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
//                    CascadeType.DETACH, CascadeType.REFRESH})
//    private List<Address> addresses;

//    public void add(Address address) {
//        if (addresses == null) {
//            addresses = new ArrayList<>();
//        }
//        addresses.add(address);
//         }
    @OneToMany(cascade =CascadeType.ALL)
    @JoinColumn(name = "store_account_id")
    private List<RatingOnStore> ratingOnStoreList;

    @OneToMany(cascade =CascadeType.ALL)
    @JoinColumn(name = "store_account_id")
    private List<Address> addressList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_account_id")
    private List<Price> priceList;

    @OneToMany(cascade =CascadeType.ALL)
    @JoinColumn(name = "store_account_id")
    private List<SocialMedia> socialMedia;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private List<SpecialOffer> specialOfferList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "store_account_id")
    private List<RatingOnProduct> ratingOnProductList;

    public void addRatingOnStore(RatingOnStore ratingOnStore) {
        if (ratingOnStoreList == null) {
            ratingOnStoreList = new ArrayList<>();
        }
        ratingOnStoreList.add(ratingOnStore);
    }

    public void removeRatingOnStore(RatingOnStore ratingOnStore) {
        if (ratingOnStoreList != null) {
            ratingOnStoreList.remove(ratingOnStore);
        }
    }

    public void addAddress(Address address) {
        if (addressList == null) {
            addressList = new ArrayList<>();
        }
        addressList.add(address);
    }

    public void removeAddress(Address address) {
        if (addressList != null) {
            addressList.remove(address);
        }
    }

    public void addPrice(Price price) {
        if (priceList == null) {
            priceList = new ArrayList<>();
        }
        priceList.add(price);
    }

    public void removePrice(Price price) {
        if (priceList != null) {
            priceList.remove(price);
        }
    }

    public void addSocialMedia(SocialMedia socialMediaItem) {
        if (socialMedia == null) {
            socialMedia = new ArrayList<>();
        }
        socialMedia.add(socialMediaItem);
    }

    public void removeSocialMedia(SocialMedia socialMediaItem) {
        if (socialMedia != null) {
            socialMedia.remove(socialMediaItem);
        }
    }

    public void addSpecialOffer(SpecialOffer specialOffer) {
        if (specialOfferList == null) {
            specialOfferList = new ArrayList<>();
        }
        specialOfferList.add(specialOffer);
    }

    public void removeSpecialOffer(SpecialOffer specialOffer) {
        if (specialOfferList != null) {
            specialOfferList.remove(specialOffer);
        }
    }

    public void addRatingOnProduct(RatingOnProduct ratingOnProduct) {
        if (ratingOnProductList == null) {
            ratingOnProductList = new ArrayList<>();
        }
        ratingOnProductList.add(ratingOnProduct);
    }

    public void removeRatingOnProduct(RatingOnProduct ratingOnProduct) {
        if (ratingOnProductList != null) {
            ratingOnProductList.remove(ratingOnProduct);
        }
    }

    // Getters and setters
}
