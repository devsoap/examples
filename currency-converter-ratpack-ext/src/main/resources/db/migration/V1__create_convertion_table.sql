CREATE TABLE convertions(
  id INT PRIMARY KEY AUTO_INCREMENT,
  fromCurrency VARCHAR (3),
  toCurrency VARCHAR (3),
  fromAmount DECIMAL (19,10),
  toAmount DECIMAL (19,10),
  convertionTimestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);