// === JOUW RIJKSORGANISATIES NOORD-NEDERLAND ===
const mapPins = [
    { lat: 53.2194, lng: 6.5665, title: "Belastingdienst", description: "Financiële diensten | MBO/HBO", link: "functies.html?id=belastingdienst" },
    { lat: 53.2000, lng: 6.1500, title: "DUO", description: "Onderwijsuitvoering | HBO/WO", link: "functies.html?id=duo" },
    { lat: 53.1000, lng: 6.0000, title: "CJIB", description: "Justitie | MBO/HBO", link: "functies.html?id=cjib" },
    { lat: 52.9500, lng: 6.4000, title: "RWS", description: "Infrastructuur | MBO/HBO", link: "functies.html?id=rws" },
    { lat: 53.3800, lng: 6.2200, title: "Douane", description: "Douane | MBO", link: "functies.html?id=douane" },
    { lat: 53.0500, lng: 5.8000, title: "Rechtbank Noord-Nederland", description: "Justitie | HBO/WO", link: "functies.html?id=rechtbank" },
    { lat: 53.0000, lng: 6.1000, title: "IND", description: "Immigratie | HBO", link: "functies.html?id=ind" }
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
        const marker = L.marker([pin.lat, pin.lng]).addTo(map);
        
        // Voeg label toe met organisatienaam
        marker.bindTooltip(pin.title, {
            permanent: true,
            direction: 'top',
            offset: [0, -10],
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
