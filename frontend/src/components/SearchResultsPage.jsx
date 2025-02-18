import React from 'react'
import SearchResults from './SearchResults';
import { useNavigate, useSearchParams } from 'react-router-dom';

const SearchResultsPage = () => {
    const [searchParams, setSearchParams] = useSearchParams(); 
    const query = searchParams.get("query");
    const currentPage = parseInt(searchParams.get("page")) || 1

    const navigate = useNavigate()


  return (
    <div>
      <SearchResults 
        key={query}
      />
    </div>
  )
}

export default SearchResultsPage
