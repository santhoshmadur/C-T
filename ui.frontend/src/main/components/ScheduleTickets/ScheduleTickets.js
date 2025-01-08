import React from 'react';
// import './schedule.css';

const productImages = document.querySelector('.schedule-tickets')?.getAttribute("data-imgUrl").split(',');

const bgImages = document.querySelector('.schedule-tickets')?.getAttribute("data-bg-imgurl").split(',');

 const ScheduleTickets = () => {
      const cards = [
        {
          date: 'DEC 03',
          day: 'MON',
          location: 'Salt Lake City',
          time: '7p CET',
          showButton: true
        },
        {
          date: 'DEC 05',
          day: 'WED',
          location: 'Los Angeles',
          time: '8p CET',
          showButton: true
        },
        {
          date: 'DEC 07',
          day: 'FRI',
          location: 'Chicago',
          time: '6:30p CET',
          showButton: true
        },
        {
          day: 'MON',
          location: 'Miami',
          time: '9p CET',
          showButton: true
        },
        {
          date: 'DEC 12',
          day: 'THU',
          location: 'New York',
          time: '7:30p CET',
          showButton: true
        },
        {
          date: 'DEC 14',
          day: 'SAT',
          location: 'Houston',
          time: '8p CET',
          showButton: true
        },
        {
          date: 'DEC 16',
          day: 'MON',
          location: 'Dallas',
          time: '6p CET',
          showButton: true
        },
        {
          date: 'DEC 18',
          day: 'WED',
          location: 'Boston',
          time: '7p CET',
          showButton: true
        },
        {
          date: 'DEC 21',
          day: 'SUN',
          location: 'Phoenix',
          time: '8:30p CET',
          showButton: true
        },
        {
          date: 'DEC 23',
          day: 'TUE',
          location: 'Denver',
          time: '7:30p CET',
          showButton: true
        },
        {
          date: 'DEC 26',
          day: 'FRI',
          location: 'Seattle',
          time: '6p CET',
          showButton: true
        },
        {
          date: 'DEC 28',
          day: 'SUN',
          location: 'Atlanta',
          time: '9p CET',
          showButton: true
        },
        {
          date: 'DEC 30',
          day: 'TUE',
          location: 'Denver',
          time: '8p CET',
          showButton: true
        },
        {
          date: 'DEC 31',
          day: 'WED',
          location: 'Los Angeles',
          time: '8p CET',
          showButton: true
        },
        {
          date: 'JAN 01',
          day: 'SAT',
          location: 'Santa Fe',
          time: '8p CET',
          showButton: true
        },
      ];

      const tickets = [
        { heading: 'FOUR PACKS', desc: 'Select any four games', price: 'Starting at $80' },
        { heading: 'QUARTER SEASON', desc: '20 games starting on selected date', price: 'Starting at $400' },
        { heading: 'HALF SEASON', desc: '41 games starting on selected date', price: 'Starting at $800' },
        { heading: 'FULL SEASON', desc: '82 regular season games plus playoffs', price: 'Starting at $1500', soldOut: true },
      ];

      const products = [
        { name: 'Inaugural Season',productName:'Wolves Hoodie', price: '$89' },
        { name: 'Inaugural Season',productName:'Wolves Jersey', price: '$129' },
        { name: 'Inaugural Season',productName:'Wolves Trucker Hat', price: '$49' },
        { name: "Men's",productName:' Wolves Tee', price: '$49' },
      ];
      
    return (
        <>
        <div className='schedule-container'>
            <div className='schedule-container__banner'>
                <div className='banner-opacity'>2024/5</div>
                <div className='banner-text'>SCHEDULE & TICKETS</div>
            </div>
            <div className="schedule-and-tickets-container">
               <div className='top-block'>
                    <div className="header">
                                <h1>SCHEDULE & TICKETS</h1>
                                <div className="controls">
                                    <div className="filter-dropdown">
                                      <div className='filter-dropdown__text'>Hide Past Games</div>
                                      <div className='filter-dropdown__toggle'></div>
                                    </div>
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
                {cards.length > 0 ? (cards.map((card, cardIndex) => (
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
                  ))): <div>No games scheduled</div>}
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
            <div className="game-container--groupticketsBlock--header">
              <h1>GROUP TICKETS</h1>
              <p>
                Looking to entertain your family, friends, co-workers or team at a Las Vegas Wolves game this year? Learn about all the Wolves can offer you - there is something for everyone at a Wolves game!
              </p>
            </div>
            <div className="game-container--groupticketsBlock--options">
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
            <div className="game-container--groupticketsBlock--contact">
              <div className="contactus">
                <p>Call Us 
                  <span className='svg-icons'>
                  <svg xmlns="http://www.w3.org/2000/svg" width="11" height="10" viewBox="0 0 11 10" fill="none">
                      <path d="M9.91667 10C8.75926 10 7.61574 9.74768 6.48611 9.24306C5.35648 8.73843 4.3287 8.02315 3.40278 7.09722C2.47685 6.1713 1.76157 5.14352 1.25694 4.01389C0.752315 2.88426 0.5 1.74074 0.5 0.583333C0.5 0.416667 0.555556 0.277778 0.666667 0.166667C0.777778 0.0555556 0.916667 0 1.08333 0H3.33333C3.46296 0 3.5787 0.0439815 3.68056 0.131944C3.78241 0.219907 3.84259 0.324074 3.86111 0.444444L4.22222 2.38889C4.24074 2.53704 4.23611 2.66204 4.20833 2.76389C4.18056 2.86574 4.12963 2.9537 4.05556 3.02778L2.70833 4.38889C2.89352 4.73148 3.11343 5.0625 3.36806 5.38194C3.62268 5.70139 3.90278 6.00926 4.20833 6.30556C4.49537 6.59259 4.7963 6.8588 5.11111 7.10417C5.42593 7.34954 5.75926 7.57407 6.11111 7.77778L7.41667 6.47222C7.5 6.38889 7.6088 6.32639 7.74306 6.28472C7.87732 6.24306 8.00926 6.23148 8.13889 6.25L10.0556 6.63889C10.1852 6.67593 10.2917 6.74306 10.375 6.84028C10.4583 6.9375 10.5 7.0463 10.5 7.16667V9.41667C10.5 9.58333 10.4444 9.72222 10.3333 9.83333C10.2222 9.94444 10.0833 10 9.91667 10ZM2.18056 3.33333L3.09722 2.41667L2.86111 1.11111H1.625C1.6713 1.49074 1.73611 1.86574 1.81944 2.23611C1.90278 2.60648 2.02315 2.97222 2.18056 3.33333ZM7.15278 8.30556C7.51389 8.46296 7.88194 8.58796 8.25694 8.68056C8.63194 8.77315 9.00926 8.83333 9.38889 8.86111V7.63889L8.08333 7.375L7.15278 8.30556Z" fill="#404040"/>
                  </svg>
                  </span>
                </p>
                <p>702-555-5555, Option 4</p>
              </div>
              <div className="contactus">
                <p>Email Us
                  <span className='svg-icons'>
                  <svg xmlns="http://www.w3.org/2000/svg" width="14" height="10" viewBox="0 0 14 10" fill="none">
                    <path d="M2 10C1.65625 10 1.36198 9.8776 1.11719 9.63281C0.872396 9.38802 0.75 9.09375 0.75 8.75V1.25C0.75 0.90625 0.872396 0.611979 1.11719 0.367188C1.36198 0.122396 1.65625 0 2 0H12C12.3438 0 12.638 0.122396 12.8828 0.367188C13.1276 0.611979 13.25 0.90625 13.25 1.25V8.75C13.25 9.09375 13.1276 9.38802 12.8828 9.63281C12.638 9.8776 12.3438 10 12 10H2ZM7 5.625L2 2.5V8.75H12V2.5L7 5.625ZM7 4.375L12 1.25H2L7 4.375ZM2 2.5V1.25V8.75V2.5Z" fill="#404040"/>
                  </svg>
                  </span>
                </p>
                <p>group@wolves.com</p>
              </div>
            </div>
            <button className="game-container--groupticketsBlock--create-event">Create Your Event</button>
          </div>
          {/* gear Up Container */}
          <div className='game-container--gearUp'>
            <div className="game-container--gearUp__gear-container">
              <div className="gear-header">
                <h1>GEAR UP FOR THE GAME!</h1>
                <a href="https://publish-p127270-e1239469.adobeaemcloud.com/content/venia/language-masters/en/home/product-landing-page.html" className="shop-all">
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
                  <div className="gear-name">{product.name}
                      <div>{product.productName}</div>
                    </div>
                    <button className="buy-button">Buy Now | {product.price}</button>
                  </div>
                </div>
              ))}
              </div>
            </div>
          </div>
        </div>
        {/* TourContainer */}
        <div className="tour-container">
              <div className='tourContainerImage'>
                <img src={bgImages[0]} alt='layout-image'/>
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
                  <ul className="arena-legend">
                    <li><span className="color-box" style={{ backgroundColor: '#F1411A' }}></span> Lower Bowl 1</li>
                    <li><span className="color-box" style={{ backgroundColor: '#532C97' }}></span> Lower Bowl 2</li>
                    <li><span className="color-box" style={{ backgroundColor: '#027F95' }}></span> Lower Bowl 3</li>
                    <li><span className="color-box" style={{ backgroundColor: '#BDC673' }}></span> Lower Bowl 4</li>
                    <li><span className="color-box" style={{ backgroundColor: '#FEDE02' }}></span> Lower Bowl 5</li>
                    <li><span className="color-box" style={{ backgroundColor: '#000000' }}></span> Center Balcony</li>
                    <li><span className="color-box" style={{ backgroundColor: '#C8C7CC' }}></span> Balcony</li>
                  </ul>
                </div>
              </div>
              <div className="arena-map">
                <img src={bgImages[1]} alt='tour-image'/>
              </div>
        </div>
        </>
     );
}
export default ScheduleTickets;