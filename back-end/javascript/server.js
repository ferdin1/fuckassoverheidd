const express = require('express');
const mysql = require('mysql2');
const cors = require('cors');
const bodyParser = require('body-parser');

const app = express();
app.use(cors());
app.use(bodyParser.json());

// MySQL Verbinding configuratie
// PAS DIT AAN NAAR JOUW EIGEN GEGEVENS
const db = mysql.createConnection({
    host: '127.0.0.1',
    user: 'root',
    password: '', // Meestal leeg bij XAMPP
    database: 'datarijksnoord',
    port: 3306
});

db.connect((err) => {
    if (err) {
        console.error('Kon geen verbinding maken met MySQL:', err.message);
        return;
    }
    console.log('Verbonden met MySQL database!');
});

// --- API ENDPOINTS VOOR FUNCTIES ---

// Alle functies ophalen
app.get('/api/functies', (req, res) => {
    db.query('SELECT * FROM functies', (err, results) => {
        if (err) return res.status(500).json({ error: err.message });

        // Transformeren naar object key-value pair voor compatibiliteit met je front-end
        const functiesMap = {};
        results.forEach(f => {
            functiesMap[f.id] = {
                titel: f.titel,
                beschrijving: f.beschrijving,
                benodigd: f.benodigd,
                opleiding: f.opleiding,
                cursus: f.cursus,
                vervolg_functie: f.vervolg_functie
            };
        });
        res.json(functiesMap);
    });
});

// Nieuwe functie toevoegen
app.post('/api/functies', (req, res) => {
    const { titel, beschrijving, benodigd, opleiding, cursus, vervolg_functie } = req.body;
    const sql = 'INSERT INTO functies (titel, beschrijving, benodigd, opleiding, cursus, vervolg_functie) VALUES (?, ?, ?, ?, ?, ?)';
    db.query(sql, [titel, beschrijving, benodigd, opleiding, cursus, vervolg_functie], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json({ id: result.insertId, message: 'Functie toegevoegd!' });
    });
});

// --- API ENDPOINTS VOOR PINS ---

// Alle pins ophalen
app.get('/api/pins', (req, res) => {
    db.query('SELECT * FROM pins', (err, results) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json(results);
    });
});

// Nieuwe pin toevoegen
app.post('/api/pins', (req, res) => {
    const { lat, lng, functionId } = req.body;
    const sql = 'INSERT INTO pins (lat, lng, functionId) VALUES (?, ?, ?)';
    db.query(sql, [lat, lng, functionId], (err, result) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json({ id: result.insertId, message: 'Pin toegevoegd!' });
    });
});

// Pin verwijderen
app.delete('/api/pins/:id', (req, res) => {
    db.query('DELETE FROM pins WHERE id = ?', [req.params.id], (err) => {
        if (err) return res.status(500).json({ error: err.message });
        res.json({ message: 'Pin verwijderd!' });
    });
});

const PORT = 3000;
app.listen(PORT, () => {
    console.log(`Server draait op http://localhost:${PORT}`);
});
