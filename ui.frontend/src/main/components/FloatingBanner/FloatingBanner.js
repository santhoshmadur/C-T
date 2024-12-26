import React from 'react';

const FloatingBanner = () => {

  const bannerText = document.querySelector(".floating-banner")?.getAttribute("data-bannerText");
    const items = Array.from({ length: 10 }, (_, index) => bannerText);

  return (
    <div className='banner'>
      <div className='scrolling-content'>
        {items.map((item, index) => (
          <div className='item' key={index}>{item}</div>
        ))}
        {items.map((item, index) => (
          <div className='item' key={index + items.length}>{item}</div>
        ))}
      </div>
    </div>
  );
}

export default FloatingBanner;