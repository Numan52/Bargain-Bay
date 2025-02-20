package org.example.app.Services.AdsFetching;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AdFetchingFilter {
    private int limit;
    private int offset;
    private String query;
    private UUID userId;

    public AdFetchingFilter(Builder builder) {
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.query = builder.query;
        this.userId = builder.userId;
    }



    public static class Builder {
        private int limit = 20;
        private int offset = 0;
        private String query = "";
        private UUID userId = null;

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder query(String query) {
            this.query = query;
            return this;
        }

        public Builder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public AdFetchingFilter build() {
            return new AdFetchingFilter(this);
        }
    }
}
