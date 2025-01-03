import React from 'react';
// import './schedule.css';

const productImages = document.querySelector('.schedule-tickets').getAttribute("data-imgUrl").split(',');

const bgImages = document.querySelector('.schedule-tickets').getAttribute("data-bg-imgurl").split(',');;

 const ScheduleTickets = () => {
      const cards = [
        {
          date: 'DEC 03',
          day: 'MON',
          location: 'Salt Lake City',
          time: '7p CET'
        },
        {
          date: 'DEC 05',
          day: 'WED',
          location: 'Los Angeles',
          time: '8p CET'
        },
        {
          date: 'DEC 07',
          day: 'FRI',
          location: 'Chicago',
          time: '6:30p CET'
        },
        {
          day: 'MON',
          location: 'Miami',
          time: '9p CET'
        },
        {
          date: 'DEC 12',
          day: 'THU',
          location: 'New York',
          time: '7:30p CET'
        },
        {
          date: 'DEC 14',
          day: 'SAT',
          location: 'Houston',
          time: '8p CET'
        },
        {
          date: 'DEC 16',
          day: 'MON',
          location: 'Dallas',
          time: '6p CET'
        },
        {
          date: 'DEC 18',
          day: 'WED',
          location: 'Boston',
          time: '7p CET'
        },
        {
          date: 'DEC 21',
          day: 'SUN',
          location: 'Phoenix',
          time: '8:30p CET'
        },
        {
          date: 'DEC 23',
          day: 'TUE',
          location: 'Denver',
          time: '7:30p CET'
        },
        {
          date: 'DEC 26',
          day: 'FRI',
          location: 'Seattle',
          time: '6p CET'
        },
        {
          date: 'DEC 28',
          day: 'SUN',
          location: 'Atlanta',
          time: '9p CET'
        },
        {
          date: 'DEC 30',
          day: 'TUE',
          location: 'Denver',
          time: '8p CET'
        },
        {
            date: 'DEC 31',
            day: 'WED',
            location: 'Los Angeles',
            time: '8p CET'
        },
        {
            date: 'JAN 01',
            day: 'SAT',
            location: 'Santa Fe',
            time: '8p CET'
        },
      ];

      const tickets = [
        { heading: 'FOUR PACKS', desc: 'Select any four games', price: 'Starting at $80' },
        { heading: 'QUARTER SEASON', desc: '20 games starting on selected date', price: 'Starting at $400' },
        { heading: 'HALF SEASON', desc: '41 games starting on selected date', price: 'Starting at $800' },
        { heading: 'FULL SEASON', desc: '82 regular season games plus playoffs', price: 'Starting at $1500', soldOut: true },
      ];

      const products = [
        { name: 'Inaugural Season Wolves Hoodie', price: '$89' },
        { name: 'Inaugural Season Wolves Jersey', price: '$129' },
        { name: 'Inaugural Season Wolves Trucker Hat', price: '$49' },
        { name: "Men's Wolves Tee", price: '$49' },
      ];
      
    return (
        <>
        <div className='schedule-container'>
            <div className='schedule-container__banner'>
                <div className='banner-opacity'>2025/6</div>
                <div className='banner-text'>SCHEDULE TICKETS</div>
            </div>
            <div className="schedule-and-tickets-container">
               <div className='top-block'>
                    <div className="header">
                                <h1>SCHEDULE & TICKETS</h1>
                                <div className="controls">
                                    <button className="filter-sort-button">Filter & Sort</button>
                                </div>
                    </div>
                    <div className="info">
                        <p>Purchase your tickets in advance to save on ticket costs and avoid game-day lines!</p>
                        <p>Single game tickets are discounted for advance sales.</p>
                        <p>Advance sales include any purchase online, over the phone, or in person at the Thomas & Mack Center Box Office prior to the day of the event.</p>
                        <p>Day of game prices increase by $10 in every price level.</p>
                    </div>
               </div>
               <div className='card-block'>
                {cards.map((card, cardIndex) => (
                      <div className="scheduleCard" key={cardIndex}>
                          <h2 className='scheduleCard--day'>{card.day}</h2>
                          <p className='scheduleCard--date'>{card.date}</p>
                          <span className='scheduleCard--vs'>vs</span> 
                          <div>
                              <svg xmlns="http://www.w3.org/2000/svg" width="72" height="72" viewBox="0 0 72 72" fill="none">
                                  <circle cx="36" cy="36" r="36" fill="white"/>
                              </svg>
                          </div>
                          <span className='scheduleCard--location'>{card.location}</span>
                          <p className='scheduleCard--time'>{card.time}</p>
                          { 
                          card.showButton ? 
                          <button>Buy Tickets</button>
                          : null
                        }
                      </div>
                  ))}
               </div>
            </div>
        </div>
        <div className='game-container'>
        {/* Multi-Game and Season Tickets */}
        <div className="game-container--textBlock">
          <h1>MULTI-GAME AND SEASON TICKETS</h1>
          <h2>In it to win it!</h2>
          <div className='text-block-max-font'>Follow @LasVegasWolves for exclusive highlights, behind-the-scenes moments, and all the Wolves howl you can handle!</div>
          <div className='text-block-max-font mini-font'> Price levels and quantity can vary by game </div>
        </div>
          <div className='game-container--ticketsBlock'>
            <div className='game-container--ticketsBlockCard'>
              {tickets.map((card, index)=> (
                <div className='ticketCard' key={index}>
                  <div className='heading'>{card.heading}</div>
                  <div className='description'>{card.desc}</div>
                  <div className='price'>{card.price}</div>
                  {card.soldOut ? (
                    <div>SOLD OUT</div>
                  ) : (
                    <button>Buy Tickets</button>
                  )}
                </div>
              ))}
            </div>
          </div>
          <div className="game-container--groupticketsBlock">
            <div className="groupticketsBlock--header">
              <h1>GROUP TICKETS</h1>
              <p>
                Looking to entertain your family, friends, co-workers or team at a Las Vegas Wolves game this year? Learn about all the Wolves can offer you - there is something for everyone at a Wolves game!
              </p>
            </div>
            <div className="groupticketsBlock--options">
              <div className="option">
                <h3>DEDICATED REPRESENTATIVE</h3>
                <p>Plan the perfect night for your group alongside one of our experienced group team members.</p>
              </div>
              <div className="option">
                <h3>UNIQUE FAN EXPERIENCE</h3>
                <p>Enjoy special invites and perks for events before and after game time.</p>
              </div>
              <div className="option">
                <h3>PREFERRED GROUP PRICING</h3>
                <p>Enjoy a savings of 10% off single game pricing when you book a group of 10 or more.</p>
              </div>
            </div>
            <div className="groupticketsBlock--contact">
              <div className="contactus">
                <p>Call Us</p>
                <p>702-555-5555, Option 4</p>
              </div>
              <div className="contactus">
                <p>Email Us</p>
                <p>group@wolves.com</p>
              </div>
            </div>
            <button className="create-event">Create Your Event</button>
          </div>
          {/* gear Up Container */}
          <div className='game-container--gearUp'>
            <div className="gear-container">
              <div className="gear-header">
                <h1>GEAR UP FOR THE GAME!</h1>
                <a href="#" className="shop-all">
                  SHOP ALL &gt;
                </a>
              </div>
              <div className="gear-grid">
              {products.map((product, index) => (
                <div className="gear-item" key={index}>
                  <div className="gear-image">
                    <img src={productImages[index]} alt={product.name} />
                  </div>
                  <div className="gear-info">
                    <div className="gear-name">{product.name}</div>
                    <button className="buy-button">Buy Now | {product.price}</button>
                  </div>
                </div>
              ))}
              </div>
            </div>
          </div>
            {/* TourContainer */}
           <div className="game-container--tourContainer">
              <div className='tourContainerImage' style={{backgroundImage: `url(${bgImages[0]})`}}></div>
              <div className='tourtext-content'>
                <div className='text-head'>
                  <span>Thomas & Mack Center</span>
                  <address>
                      4505 South Maryland Parkway<br />
                      Las Vegas, NV 89154-0003<br />
                      (702) 739-FANS (3267)<br />
                      1-866-388-FANS (3267)<br />
                      <a href="mailto:customer.service@unlvtickets.com">
                        customer.service@unlvtickets.com
                      </a>
                    </address>
                </div>
                <div className="arena-info-address">
                    <ul className="arena-legend">
                      <li><span className="color-box" style={{ backgroundColor: 'orange' }}></span> Lower Bowl 1</li>
                      <li><span className="color-box" style={{ backgroundColor: 'purple' }}></span> Lower Bowl 2</li>
                      <li><span className="color-box" style={{ backgroundColor: 'yellow' }}></span> Lower Bowl 3</li>
                      <li><span className="color-box" style={{ backgroundColor: 'lightgreen' }}></span> Lower Bowl 4</li>
                      <li><span className="color-box" style={{ backgroundColor: 'cyan' }}></span> Lower Bowl 5</li>
                      <li><span className="color-box" style={{ backgroundColor: 'black' }}></span> Center Balcony</li>
                      <li><span className="color-box" style={{ backgroundColor: 'gray' }}></span> Balcony</li>
                    </ul>
                  </div>
              </div>
              <div className="arena-map" style={{backgroundImage: `url(${bgImages[1]})`}}>
              </div>
            </div>
        </div>
        </>
     );
}
export default ScheduleTickets;