// Do not directly import these from your files. This allows the autograder to evaluate the functions in this
// file against the sample solution.
import { fetchCurrentTemperature, fetchGeoCoord, fetchUniversities } from "../include/exports.js";

/* FROM SPEC:
interface AverageTemperatureResults {
  totalAverage: number;
  [key: string]: number;
}

interface GeoCoord {
    lon: number,
    lat: number,
    importances: number[]
}
  */

//fetchUniversities -> list of universities -> list.foreach(fetchGeoCoord(university)) -> lon & lat -> fetchCurrTemp(lon & lat) --:

export function fetchUniversityWeather(universityQuery, transformName) {
  //prevents infinite loop iff query is empty
  if (universityQuery === "") {
    return Promise.reject(new Error("No results found for query."));
  }
  // fetchUniversities
  return (
    fetchUniversities(universityQuery)
      .then(arrOfNames => {
        return arrOfNames.map(name => ({ originalName: name, value: transformName ? transformName(name) : name }));
      })
      // fetchGeoCoords
      .then(arrNamesToTransformedNames => {
        return Promise.all(
          arrNamesToTransformedNames.map(obj => {
            return fetchGeoCoord(obj.value).then(value1 => ({ name: obj.originalName, value1 }));
          })
        );
      })
      //fetchCurrentTemperature
      .then(arrNamesToGeoCoords => {
        return Promise.all(
          arrNamesToGeoCoords.map(obj => {
            return fetchCurrentTemperature(obj.value1).then(value2 => ({ name: obj.name, value2 }));
          })
        );
      })
      .then(arrGeoCoordsToTemps => {
        const arrTempsToAvg = arrGeoCoordsToTemps.map(obj => {
          const avgTemp = obj.value2.temperature_2m.reduce((acc, e) => acc + e, 0) / obj.value2.temperature_2m.length;
          return { name: obj.name, value3: avgTemp };
        });
        const keyValPairs = arrTempsToAvg.reduce((acc, e) => {
          acc[e.name] = e.value3;
          return acc;
        }, {});
        const totalAverage = arrTempsToAvg.reduce((acc, e) => acc + e.value3, 0) / arrTempsToAvg.length;
        // if (keyValPairs.length === 0 && totalAverage === 0 || totalAverage !== totalAverage) Promise.reject(new Error("No results found for query."));
        // return Promise.resolve({ totalAverage, ...keyValPairs }); //spread operator!!!

        if (!((keyValPairs.length === 0 && totalAverage === 0) || totalAverage !== totalAverage)) {
          //NaN !== NaN returns true ;D
          return Promise.resolve({ totalAverage, ...keyValPairs }); //spread operator!!!
        } else {
          return Promise.reject(new Error("No results found for query."));
        }
      })
      //error handling
      .catch(error => Promise.reject(error))
  );
}

export function fetchUMassWeather() {
  function removeAt(str) {
    return str.includes(" at ") ? str.replace(" at ", " ") : str;
  }
  return fetchUniversityWeather("University of Massachusetts", removeAt);
}

export function fetchUMichWeather() {
  return fetchUniversityWeather("University of Michigan");
}
