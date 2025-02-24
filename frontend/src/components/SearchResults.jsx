import React, { useEffect, useRef, useState } from 'react'
import Header from './Header'
import { useNavigate, useSearchParams } from 'react-router-dom';
import { getSearchedAds } from '../api/adsApi';
import SearchResultAds from './SearchResultAds';
import Pagination from './Pagination';
import Searchbar from './Searchbar';
import "../css/searchbar.css"
import { useMemo } from 'react';


const SearchResults = () => {
    const [searchParams, setSearchParams] = useSearchParams(); 
    const query = searchParams.get("query");
    const currentPage = parseInt(searchParams.get("page")) || 1

    const [allFetchedAds, setAllFetchedAds] = useState({})
    const [fetchedPages, setFetchedPages] = useState([])
    const [totalAds, setTotalAds] = useState(0)
    const adsPerPage = 10
    const isFirstRender = useRef(true)
    const navigate = useNavigate()

    const setPage = (newPage) => {
      setSearchParams({query, page: newPage})
    }

    console.log(allFetchedAds)


    

  
    let adsToDisplay = useMemo(() => {
      return allFetchedAds[currentPage] || []
     
    }, [currentPage, allFetchedAds]) 
    

    function handleSearch(input) {
      console.log(input)
      if (input !== null && input.trim().length > 0) {
        navigate(`/search?query=${encodeURIComponent(input)}`)
      }
      
    }


  
    useEffect(() => {
      if (isFirstRender.current) {
        isFirstRender.current = false
        return
      }

      if (fetchedPages.includes(currentPage) || !query) {
        return
      }

      async function fetchSearchResults() {
        const offset = (currentPage * adsPerPage) - adsPerPage

        console.log(query)
        console.log(offset)
        console.log(adsPerPage)

        
        try {
          const {ads, totalAds: newTotalAds} = await getSearchedAds(query, offset, adsPerPage)
          setAllFetchedAds((prev) => ({
              ...prev,
              [currentPage]: ads
            }
          )
          )
          setFetchedPages((prev) => [...prev, currentPage])
          if (totalAds === 0) {
            setTotalAds(newTotalAds)
          }

        } catch (error) {
          console.log(error)
        }
      }
      
      fetchSearchResults()
    }, [query, currentPage])

    
    return (
    <div>
      <Header />
      <div className='search-results__container'>
        <Searchbar onSearch={(input) => handleSearch(input)}/>

        <h2>
          {`${totalAds} Ads found`}
        </h2>
        
        <div className='search-results__ads-container'>
          <SearchResultAds ads={adsToDisplay}/>
          <Pagination totalAds={totalAds} page={currentPage} setPage={setPage} adsPerPage={adsPerPage}/>
        </div>
        
      </div>
      
      
    </div>
  )
}

export default SearchResults
