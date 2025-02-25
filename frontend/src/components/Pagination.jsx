import React from 'react'

const Pagination = ({totalAds, currentPage, setPage, adsPerPage}) => {

    const pageNumbers = []
    let totalPages = Math.ceil(totalAds / adsPerPage)
    for (let i = 1; i <= totalPages; i++) {
        pageNumbers.push(i)
    }

    function handleClick(e, number) {
        e.preventDefault()
        setPage(number)
    }


    console.log(currentPage)
    

    return (
        <nav>
          <ul className="pagination">
            {pageNumbers.map((number) => (
              <li
                key={number}
                onClick={(e) => handleClick(e, number)} 
                className={`page-item ${currentPage === number ? "active" : ""}`}
              >
                <a
                  
                //   href='!#'
                  className="pagination__page-link"
                >
                  {number}
                </a>

              </li>
            ))}
          </ul>
        </nav>
      );
}

export default Pagination
