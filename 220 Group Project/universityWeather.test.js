import { jest } from "@jest/globals";
import { fetchUMichWeather, fetchUMassWeather, fetchUniversityWeather } from "./universityWeather.js";
import assert from "assert";

const SECOND = 1000;
jest.setTimeout(40 * SECOND);

describe("fetchUMichWeather", () => {
  // TODO
  it("follows type specification", () => {
    const promise = fetchUMichWeather();

    return promise.then(result => {
      assert(typeof result === "object", `result has type ${typeof result}`); //  Assert the result is an object
      assert(typeof result.totalAverage === "number");
      assert(Object.keys(result).length > 1); // 1 key is totalAverage, but should have at least 1 other associated key (a university)
      // assert(result.keyValPairs)
    });
  });
});

describe("fetchUMassWeather", () => {
  // TODO
  it("follows type specification", () => {
    const promise = fetchUMassWeather();

    return promise.then(result => {
      assert(typeof result === "object", `result has type ${typeof result}`); //  Assert the result is an object
      assert(typeof result.totalAverage === "number", `Received ${result.totalAverage}`);
      assert(Object.keys(result).length > 1); // 1 key is totalAverage, but should have at least 1 other associated key (a university)
    });
  });
});

describe("fetchUniversityWeather", () => {
  it("follows type specification", () => {
    const promise = fetchUniversityWeather("University of Michigan");

    return promise.then(result => {
      assert(typeof result === "object", `result has type ${typeof result}`); //  Assert the result is an object
      assert(typeof result.totalAverage === "number");
      assert(Object.keys(result).length > 1); // 1 key is totalAverage, but should have at least 1 other associated key (a university)
    });
  });

  it("Rejects when given an empty query", async () => {
    await expect(fetchUniversityWeather("")).rejects.toThrow("No results found for query.");
  });

  it("Rejects when given a place that doesn't exist", async () => {
    await expect(fetchUniversityWeather("This Place Does Not Exist")).rejects.toThrow("No results found for query.");
  });
});
