export function fetchCurrentTemperature(coords) {
  // TODO
  if (coords.lat === undefined || coords.lon === undefined) return Promise.reject(new Error("Undefined coords."));
  const url = `https://220.maxkuechen.com/currentTemperature/forecast?latitude=${coords.lat}&longitude=${coords.lon}&hourly=temperature_2m&temperature_unit=fahrenheit`;
  return fetch(url)
    .then(response => {
      if (!response.ok) {
        return Promise.reject(new Error(response.statusText));
      }
      return response.json();
    })
    .then(data => {
      const time = data.hourly.time;
      const temperature_2m = data.hourly.temperature_2m;
      if (time !== undefined && temperature_2m !== undefined) return Promise.resolve({ time, temperature_2m });
      else return Promise.reject(new Error("undefined"));
    })
    .catch(error => Promise.reject(error));
}

export function tempAvgAboveAtCoords(coords, temp) {
  // TODO
  return fetchCurrentTemperature(coords).then(data => {
    const temps = data.temperature_2m;
    if (temps.length === 0) {
      return false;
    }
    const sum = temps.reduce((acc, e) => acc + e, 0);
    const avg = sum / temps.length;
    return Promise.resolve(avg > temp);
  });
}
