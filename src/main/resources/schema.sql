CREATE TABLE nim_persistence (
  id INT PRIMARY KEY,
  initialPileSize INT,
  maxNimCount INT,
  lastComputerMove INT,
  currentPileSize INT,
  isPlayersTurn BIT
);