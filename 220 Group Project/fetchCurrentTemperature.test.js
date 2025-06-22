import { jest } from "@jest/globals";
import fetchMock from "jest-fetch-mock";
import { fetchCurrentTemperature, tempAvgAboveAtCoords } from "./fetchCurrentTemperature.js";
import assert from "assert";

const SECOND = 1000;
jest.setTimeout(30 * SECOND);

global.fetch = jest.fn();

describe("fetchCurrentTemperature", () => {
  // TODO
  beforeEach(() => {
    fetchMock.enableMocks();
  });

  it("follows type specification", () => {
    const coords = { lon: -71.0061861, lat: 41.6295897 };
    const promise = fetchCurrentTemperature(coords);

    return promise.then(result => {
      assert(typeof result === "object");
      assert(Array.isArray(result.time));
      assert(Array.isArray(result.temperature_2m));
      assert(Array.isArray(result.time));
      assert(typeof result.temperature_2m[0] === "number");
      assert(typeof result.time[0] === "string");
      assert(Object.keys(result).length === 2);
    });
  });
  it("should return correct on fetch", async () => {
    const mockResponse = {
      hourly: {
        time: ["2024-11-19T00:00", "2024-11-19T01:00"],
        temperature_2m: [34.2, 33.7],
      },
    };

    fetchMock.mockResponse(JSON.stringify(mockResponse));

    const coords = { lat: 40, lon: 40 };
    const result = await fetchCurrentTemperature(coords);

    expect(result).toEqual({
      time: ["2024-11-19T00:00", "2024-11-19T01:00"],
      temperature_2m: [34.2, 33.7],
    });
  });
  it("should reject on failure", async () => {
    fetchMock.mockResolvedValue({
      ok: false,
      statusText: "Not Found",
      json: () => Promise.resolve({}),
    });

    const coords = { lat: 40, lon: 40 };

    await expect(fetchCurrentTemperature(coords)).rejects.toThrow("Not Found");
  });
  it("test fetch error", async () => {
    fetchMock.mockRejectedValue(new Error("Fetch Error"));

    const coords = { lat: 40.7128, lon: -74.006 };

    await expect(fetchCurrentTemperature(coords)).rejects.toThrow("Fetch Error");
  });
  afterEach(() => {
    fetchMock.resetMocks();
    fetchMock.disableMocks();
  });
});

describe("tempAvgAboveAtCoords", () => {
  // TODO
  beforeEach(() => {
    fetchMock.enableMocks();
  });
  it("should return true if avg is above temp", async () => {
    const mockTemperatureData = {
      hourly: {
        time: ["2024-11-19T00:00", "2024-11-19T01:00"],
        temperature_2m: [75, 80],
      },
    };

    // Mock the fetchCurrentTemperature function
    fetchMock.mockResponse(JSON.stringify(mockTemperatureData));

    const coords = { lat: 40, lon: 40 };
    const result = await tempAvgAboveAtCoords(coords, 77);

    expect(result).toBe(true);
  });

  it("should return false if avg is below temp", async () => {
    const mockTemperatureData = {
      hourly: {
        time: ["2024-11-19T00:00", "2024-11-19T01:00"],
        temperature_2m: [75, 80],
      },
    };

    // Mock the fetchCurrentTemperature function
    fetchMock.mockResponse(JSON.stringify(mockTemperatureData));

    const coords = { lat: 40, lon: 40 };
    const result = await tempAvgAboveAtCoords(coords, 78);

    expect(result).toBe(false);
  });

  it("should handle error upon fetchCurrentTemperature reject", async () => {
    // Mock fetchCurrentTemperature to reject with an error
    fetchMock.mockRejectedValue(new Error("Fetch Error"));

    const coords = { lat: 40.7128, lon: -74.006 };

    await expect(tempAvgAboveAtCoords(coords, 70)).rejects.toThrow("Fetch Error");
  });

  afterEach(() => {
    fetchMock.resetMocks();
    fetchMock.disableMocks();
  });
});
