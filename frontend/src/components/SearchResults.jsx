import React, { useEffect, useRef, useState } from 'react'
import Header from './Header'
import { useNavigate, useSearchParams } from 'react-router-dom';
import { getAds, getAdsByCategory, getAdsByQuery } from '../api/adsApi';
import SearchResultAds from './SearchResultAds';
import Pagination from './Pagination';
import Searchbar from './Searchbar';
import "../css/searchbar.css"
import { useMemo } from 'react';


const SearchResults = ({query, currentPage, categoryId, category}) => {
    

    const [allFetchedAds, setAllFetchedAds] = useState({})
    const [fetchedPages, setFetchedPages] = useState([])
    const [totalAds, setTotalAds] = useState(0)
    const adsPerPage = 10
    
    const [searchParams, setSearchParams] = useSearchParams();
    const navigate = useNavigate()
    
    const setPage = (newPage) => {
      setSearchParams((prev) => {
        const newParams = new URLSearchParams(prev)
        newParams.set("page", newPage)
        return newParams
      })
      
      window.scrollTo(0,0)
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
      
      if (fetchedPages.includes(currentPage) || (!query && !categoryId && !category)) {
        
        return
      }

      async function fetchSearchResults() {
        const offset = (currentPage * adsPerPage) - adsPerPage

        console.log(query)
        console.log(offset)
        console.log(adsPerPage)


        try {
          let ads, newTotalAds, result;

          if (category === "all") {
            result = await getAds(offset, adsPerPage);
          } else if (query) {
            result = await getAdsByQuery(query, offset, adsPerPage);
          } else {
            result = await getAdsByCategory(categoryId, offset, adsPerPage);
          }

          ads = result.ads;
          newTotalAds = result.totalAds;

          console.log("fetched ads :", ads)
 
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
    }, [query, currentPage, categoryId])

    
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
          <Pagination totalAds={totalAds} currentPage={currentPage} setPage={setPage} adsPerPage={adsPerPage}/>
        </div>
        
      </div>
      
      
    </div>
  )
}

export default SearchResults
