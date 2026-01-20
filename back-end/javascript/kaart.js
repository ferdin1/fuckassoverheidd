// === JOUW RIJKSORGANISATIES NOORD-NEDERLAND ===
const mapPins = [
    { lat: 53.2129, lng: 6.5626, title: "Belastingdienst", description: "Financiële diensten | MBO/HBO", link: "functies.html?id=belastingdienst" },
    { lat: 53.2100, lng: 6.5600, title: "DUO", description: "Onderwijsuitvoering | HBO/WO", link: "functies.html?id=duo" },
    { lat: 53.2010, lng: 5.7985, title: "CJIB", description: "Justitie | MBO/HBO", link: "../front-end/cjibpagina.html" },
    { lat: 52.5929, lng: 6.5623, title: "RWS", description: "Infrastructuur | MBO/HBO", link: "../front-end/rwspagina.html" },
    { lat: 53.2200, lng: 6.6000, title: "Douane", description: "Douane | MBO", link: "functies.html?id=douane" },
    { lat: 53.2008, lng: 5.7997, title: "Rechtbank Noord-Nederland", description: "Justitie | HBO/WO", link: "functies.html?id=rechtbank" },
    { lat: 53.2130, lng: 6.5630, title: "IND", description: "Immigratie | HBO", link: "functies.html?id=ind" }
    { lat: 53.2600, lng: 6.6200, title: "RDW", description: "Voertuigregistratie | MBO/HBO", link: "rdw.html" },

];

// === KAART INITIALISATIE ===
const map = L.map('map').setView([53.1, 6.5], 9); // Noord-Nederland centrum

L.tileLayer('https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png', {
    attribution: '© OpenStreetMap contributors © CARTO',
    subdomains: 'abcd',
    maxZoom: 19
}).addTo(map);

// === ALLEEN NOORD-NEDERLAND TOEGESTAAN ===
const northBounds = L.latLngBounds(
    [52.8, 3.5],  // Zuidwest (Friesland)
    [53.7, 7.2]   // NoordOost (Groningen)
);
map.setMaxBounds(northBounds);
map.on('drag', function() {
    if (!map.getBounds().intersects(northBounds)) {
        map.panInsideBounds(northBounds, {animate: false});
    }
});

// === MARKERS TONEN ===
let markers = [];

function renderPins() {
    // Verwijder oude markers
    markers.forEach(m => map.removeLayer(m));
    markers = [];

    // Nieuwe markers voor elke organisatie
    mapPins.forEach(pin => {
        // Create Custom Building Icon with Photo
        const buildingIcon = L.divIcon({
            html: `<div class="building-marker" style="background-image: url('../front-end/images/gebouw.avif'); background-size: cover; background-position: center;"></div>`,
            iconSize: [50, 50],
            iconAnchor: [25, 50],
            popupAnchor: [0, -50],
            className: 'building-icon-wrapper'
        });

        const marker = L.marker([pin.lat, pin.lng], { icon: buildingIcon }).addTo(map);
        
        // Voeg label toe met organisatienaam
        marker.bindTooltip(pin.title, {
            permanent: true,
            direction: 'top',
            offset: [0, -50],
            className: 'marker-label'
        });
        
        const popupContent = `
            <div class="popup-header">${pin.title}</div>
            <div class="popup-body">
                <div style="font-size: 12px; color: #666; margin-bottom: 8px;">${pin.description}</div>
                <a href="${pin.link}" class="popup-btn">Bekijk Functies</a>
            </div>
        `;
        
        marker.bindPopup(popupContent, { className: 'custom-popup' });
        markers.push(marker);
    });
}

// === INITIALISATIE ===
renderPins();

// === BEWERK MODUS (optioneel, behoud je admin functionaliteit) ===
let isEditMode = false;
document.getElementById('editModeToggle').addEventListener('change', (e) => {
    isEditMode = e.target.checked;
    document.body.classList.toggle('edit-mode', isEditMode);
});

// Toast functie voor feedback
function showToast(msg) {
    const toast = document.getElementById('toast');
    toast.textContent = msg;
    toast.style.display = 'block';
    setTimeout(() => toast.style.display = 'none', 3000);
}
