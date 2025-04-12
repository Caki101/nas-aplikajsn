package com.backend.Backend.unsplashIntegration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UnsplashResponse {
    List<Photo> results;

    public List<Photo> getResults() {
        return results;
    }

    public void setResults(List<Photo> results) {
        this.results = results;
    }

    public static class Photo {
        Urls urls;

        public Urls getUrls() {
            return urls;
        }

        public void setUrls(Urls urls) {
            this.urls = urls;
        }

        public static class Urls {
            @JsonProperty("regular")
            String regular;

            public String getRegular() {
                return regular;
            }

            public void setRegular(String regular) {
                this.regular = regular;
            }
        }
    }
}
