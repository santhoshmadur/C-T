import React from 'react';

const FloatingBanner = (props) => {
    const items = Array.from({ length: 10 }, (_, index) => props.bannerText);

  return (
    <div className='banner'>
      <div className='scrolling-content'>
        {items.map((item, index) => (
          <div className='item' key={index}>{item}</div>
        ))}
        {/* Duplicate the items for seamless scrolling */}
        {items.map((item, index) => (
          <div className='item' key={index + items.length}>{item}</div>
        ))}
      </div>
    </div>
  );
}

export default FloatingBanner;