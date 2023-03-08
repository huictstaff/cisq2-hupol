const {faker} = require("@faker-js/faker");

const crypto = require("crypto");
const fs = require("fs");

const ELECTION_COUNT = 2;
const VOTE_COUNT = 250_000;

const OUTPUT_DIR = `${__dirname}/../db/examples`;
const CANDIDATES_FILE = `${OUTPUT_DIR}/candidates.csv`;
const VOTES_FILE = `${OUTPUT_DIR}/votes.csv`;

const makeId = (input) => crypto.createHash('md5').update(input).digest("hex").slice(0, 8);

const martianRegions = [
    'Aeolis',
    'Arabia',
    'Arcadia',
    'Chryse',
    'Elysium',
    'Hellas',
    'Hesperia',
    'Icaria',
    'Lucania',
    'Mare Acidalium',
    'Olympus',
    'Phlegra',
    'Syria',
    'Tharsis',
    'Tyrrhena',
    'Utopia'
];

const factions = ["Martian Freedom", "Back2Earth", "Red Rock", "Kurinti", "Clarity"];
const candidateSeed = [
    ["Elron Husky", factions[0]],
    ["Oshari Letux", factions[1]],
    ["Tuss Rock", factions[2]],
    ["Job Kurinti", factions[3]],
    ["Hulge Rohde", factions[4]],
];

const extraCandidates = 35;
for (let i = 0; i < extraCandidates; i++) {
    candidateSeed.push([`${faker.name.firstName()} ${faker.name.lastName()}`, faker.helpers.arrayElement(factions)]);
}

const candidates = candidateSeed.map(([name, faction]) => [makeId(name), name, faction]);

if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR);
}
if (fs.existsSync(CANDIDATES_FILE)) {
    fs.rmSync(CANDIDATES_FILE);
}
if (fs.existsSync(VOTES_FILE)) {
    fs.rmSync(VOTES_FILE);
}

for (let i = 0; i < ELECTION_COUNT; i++) {
    candidates
        .forEach(([candidateId, name, faction]) => {
            fs.appendFileSync(CANDIDATES_FILE, `${candidateId};${i};${name};${faction}\n`);
        });
}

for (let i = 0; i < VOTE_COUNT; i++) {
    const electionId = faker.datatype.number({min: 0, max: ELECTION_COUNT});
    const [candidateId, _] = faker.helpers.arrayElement(candidates);
    const castDate = faker.date.between("2100-04-24", "2100-05-30").toISOString().split('T')[0];
    const region = faker.helpers.arrayElement(martianRegions);
    fs.appendFileSync(VOTES_FILE, `${electionId};${i};${candidateId};${castDate};${region}\n`);
}