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

    return (
        <nav>
          <ul className="pagination">
            {pageNumbers.map((number) => (
              <li
                key={number}
                className={`page-item ${currentPage === number ? "active" : ""}`}
              >
                <a
                  onClick={(e) => handleClick(e, number)} 
                //   href='!#'
                  className="page-link"
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
