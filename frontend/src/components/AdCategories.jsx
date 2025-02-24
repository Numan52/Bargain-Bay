import React, { useEffect, useMemo, useState } from 'react'
import { getAdCategories } from '../api/adsApi'
import "../css/categories.css"
import { Link, useNavigate } from 'react-router-dom'

const AdCategories = () => {
  const [allCategories, setAllCategories] = useState([])
  const [selectedCategory, setSelectedCategory] = useState(null)
  const [categoryPath, setCategoryPath] = useState([])
  const navigate = useNavigate()
  

  const categoriesToDisplay = useMemo(() => {
    return selectedCategory ? selectedCategory.childCategories : allCategories
  }, [selectedCategory, allCategories])

  console.log("all categories: ", allCategories)
  console.log("selected category: ", selectedCategory)


  useEffect(() => {
    async function fetchAdCategories() {
      const data = await getAdCategories()
      console.log(data)
      setAllCategories(data)
    }

    fetchAdCategories()
  }, [])


  function handleCategoryClick(clickedCategory) {
    
    console.log(clickedCategory)
    setSelectedCategory(clickedCategory)

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
      <div>
        <span>
          <Link 
            onClick={() => {
              setSelectedCategory(null)
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
            <Link onClick={() => handleCategoryClick(category)} className='categories__breadcrumb'>
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

            {selectedCategory &&
              <li>
                <Link 
                  className={`categories__category-link`}
                  to={`/search?category=${selectedCategory.name}&categoryId=${selectedCategory.id}`}
                >
                  {`View all ${selectedCategory.name}`}
                </Link>
              </li>
            }
            
            {categoriesToDisplay.map((category) => 
              <li key={category.id}>
                <Link 
                  className={`categories__category-link ${selectedCategory === category.id ? 'categories__selected' : ""}` }
                  onClick={() => handleCategoryClick(category)}
                >

                  {category.name}

                </Link>
                
              </li>
            )}
        </ul>
        )}
      </>
      
    )
  }


  return (
    <div className='categories__container'>
      {displayCategoryPath()}
      {displayCategoriesTree()}
    </div>
  )
}

export default AdCategories
