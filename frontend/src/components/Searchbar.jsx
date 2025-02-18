import React, { useState } from 'react'
import { CiSearch } from "react-icons/ci";
import "../css/searchbar.css"

const Searchbar = ({onSearch}) => {
  const [input, setInput] = useState("")

  return (
    <div className='searchbar__container'>
        <div className='searchbar__searchbar-container'>
            <input 
                className='searchbar__input' 
                type="text" 
                placeholder='What are you looking for?'
                value={input}
                onChange={(e) => setInput(e.target.value)} 
            />
            <button className='searchbar__search-button' onClick={() => onSearch(input)}>
                <CiSearch size={24}/>
            </button>
        </div>

    </div>
  )
}

export default Searchbar
