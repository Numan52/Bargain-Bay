import React, { useEffect, useMemo, useState } from 'react'
import { getAdCategories } from '../api/adsApi'
import "../css/categories.css"
import { Link, useNavigate } from 'react-router-dom'
import Separator from './Separator'


const AdCategories = ({selectedCategory, onCategorySelected, jumpToSearch}) => {
  const [allCategories, setAllCategories] = useState([])
  const [categoryPath, setCategoryPath] = useState([])
  
  

  const categoriesToDisplay = useMemo(() => {
    return selectedCategory ? selectedCategory.childCategories : allCategories
  }, [selectedCategory, allCategories])

  


  useEffect(() => {
    async function fetchAdCategories() {
      const data = await getAdCategories()
      
      setAllCategories(data)
    }

    fetchAdCategories()
  }, [])



  function handleCategoryClick(clickedCategory) {
    
    console.log(clickedCategory)
    onCategorySelected(clickedCategory)

    setCategoryPath((prev) => {
      const index = prev.findIndex((category) => category.id === clickedCategory.id)

      if (index >= 0) {
        return prev.slice(0, prev.indexOf(clickedCategory) + 1)
        
      } else {
        return [
          ...prev, clickedCategory
        ]
      }
    })
  }

  


  function displayCategoryPath() {
    if (!selectedCategory) return null

    return (
      <div className='categories__breadcrumb-container'>
        
        <span>
          <Link 
            onClick={() => {
              onCategorySelected(null)
              setCategoryPath([])
            }} 
            className='categories__breadcrumb'
          >
            All Categories
          </Link>
          {"  ---  "}
          
        </span>

        {categoryPath.map((category, index) => 
          <span key={category.id}>
            <Link 
              onClick={() => handleCategoryClick(category)} 
              className={`categories__breadcrumb ${category.id === selectedCategory?.id && "categories__breadcrumb-selected"} `}
              >
              {category.name}
            </Link>
            {index < categoryPath.length - 1 && " --- "}
          </span>
        )}
      </div>
      
    )
  }




  function displayCategoriesTree() {

    return (
      <>
        
        {(
          <ul>

            {selectedCategory && jumpToSearch &&
              <li>
                <Link 
                  className={`categories__category-link`}
                  to={`/search?category=${selectedCategory.name}&categoryId=${selectedCategory.id}`}
                >
                  {`View all in ${selectedCategory.name}`}
                </Link>
              </li>
            }
            
            {categoriesToDisplay.map((category) => 
              <li key={category.id}>
                <Link 
                  className={`categories__category-link ${selectedCategory?.id === category.id ? 'categories__selected' : ""}` }
                  onClick={() => handleCategoryClick(category)}
                >

                  {category.name}

                </Link>
                
              </li>
            )}

            {jumpToSearch && !selectedCategory &&
              <li>
                <Link 
                  className={`categories__category-link`}
                  to={`/search?category=all`}
                >
                  All
                </Link>
              </li>
            }
        </ul>
        )}
      </>
      
    )
  }


  return (
    <>
      {displayCategoryPath()}
      {displayCategoriesTree()}
    </>
      
      
  )
}

export default AdCategories
