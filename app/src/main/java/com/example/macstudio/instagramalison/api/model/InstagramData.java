package com.example.macstudio.instagramalison.api.model;

/**
 * Created by macstudio on 1/2/18.
 */

public class InstagramData {
    private Images images;
    private User user;

    public Images getImages() {
        return images;
    }

    public User getUser() {
        return user;
    }

    public class User {

        private String profile_picture;

        private String full_name;

        public String getProfile_picture() {
            return profile_picture;
        }

        public String getFull_name() {
            return full_name;
        }
    }

    public class Images {

        private Standard_resolution standard_resolution;

        public Standard_resolution getStandard_resolution() {
            return standard_resolution;
        }

        public class Standard_resolution {

            private String url;

            public String getUrl() {
                return url;
            }
        }
    }
}
