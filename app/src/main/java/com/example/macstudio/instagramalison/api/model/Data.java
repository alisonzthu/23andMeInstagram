package com.example.macstudio.instagramalison.api.model;


/**
 * Created by macstudio on 12/18/17.
 */

public class Data {
    private Images images;
    private User user;

    public Images getImages() {
        return images;
    }

    public User getUser() {
        return user;
    }

    public class User {
        private String id;
        private String profile_picture;
        private String username;
        private String full_name;

        public String getId() { return id; }

        public String getUsername() { return username; }

        public String getProfile_picture() {
            return profile_picture;
        }

        public String getFull_name() {
            return full_name;
        }
    }
  // probably should come back and refactor these classes
    public class Images {
        private Standard_resolution standard_resolution;

      public Standard_resolution getStandard_resolution() {
          return standard_resolution;
      }

      public class Standard_resolution {
          public String url;

          public String getUrl() {
              return url;
          }
      }
  }
}
