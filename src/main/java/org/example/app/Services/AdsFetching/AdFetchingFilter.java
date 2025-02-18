package org.example.app.Services.AdsFetching;



public class AdFetchingFilter {
    private int limit;
    private int offset;
    private String query;

    public AdFetchingFilter(Builder builder) {
        this.limit = builder.limit;
        this.offset = builder.offset;
        this.query = builder.query;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }


    public static class Builder {
        private int limit = 20;
        private int offset = 0;
        private String query = "";

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

        public AdFetchingFilter build() {
            return new AdFetchingFilter(this);
        }
    }
}
