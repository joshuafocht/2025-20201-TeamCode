{
  "startPoint": {
    "x": 32,
    "y": 135,
    "heading": "linear",
    "startDeg": 90,
    "endDeg": 180,
    "locked": false
  },
  "lines": [
    {
      "id": "line-vnaphgu7z2",
      "name": "driveToGoal0Path",
      "endPoint": {
        "x": 48,
        "y": 96,
        "heading": "linear",
        "startDeg": 270,
        "endDeg": 135
      },
      "controlPoints": [],
      "color": "#BA9AAA",
      "locked": false,
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mlh6ajzh-c3xq0z",
      "name": "driveToArtifact1Path",
      "endPoint": {
        "x": 48,
        "y": 84,
        "heading": "linear",
        "reverse": false,
        "startDeg": 135,
        "endDeg": 180
      },
      "controlPoints": [],
      "color": "#A9999A",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mlh6e6ox-lu5dm7",
      "name": "pickupArtifact1Path",
      "endPoint": {
        "x": 16,
        "y": 84,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#9B8979",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mlh6ey4q-06jtu0",
      "name": "driveToGoal1Path",
      "endPoint": {
        "x": 48,
        "y": 96,
        "heading": "linear",
        "reverse": false,
        "startDeg": 180,
        "endDeg": 135
      },
      "controlPoints": [],
      "color": "#7895BC",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mlh6gx44-f36tq9",
      "name": "driveToArtifact2Path",
      "endPoint": {
        "x": 48,
        "y": 60,
        "heading": "linear",
        "reverse": false,
        "startDeg": 135,
        "endDeg": 180
      },
      "controlPoints": [],
      "color": "#A9BA97",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mlh6hos1-5x678j",
      "name": "pickupArtifact2Path",
      "endPoint": {
        "x": 10,
        "y": 60,
        "heading": "constant",
        "reverse": false,
        "degrees": 180
      },
      "controlPoints": [],
      "color": "#B8DBCD",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mlh6jilz-hkv7un",
      "name": "driveToGoal2Path",
      "endPoint": {
        "x": 48,
        "y": 96,
        "heading": "linear",
        "reverse": false,
        "startDeg": 180,
        "endDeg": 135
      },
      "controlPoints": [
        {
          "x": 48,
          "y": 72
        }
      ],
      "color": "#5DCD69",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    },
    {
      "id": "mlh6lrfb-413cc4",
      "name": "leavePath",
      "endPoint": {
        "x": 24,
        "y": 70,
        "heading": "linear",
        "reverse": false,
        "startDeg": 135,
        "endDeg": 0
      },
      "controlPoints": [],
      "color": "#86BACA",
      "waitBeforeMs": 0,
      "waitAfterMs": 0,
      "waitBeforeName": "",
      "waitAfterName": ""
    }
  ],
  "shapes": [
    {
      "id": "triangle-1",
      "name": "Red Goal",
      "vertices": [
        {
          "x": 144,
          "y": 70
        },
        {
          "x": 144,
          "y": 144
        },
        {
          "x": 120,
          "y": 144
        },
        {
          "x": 138,
          "y": 119
        },
        {
          "x": 138,
          "y": 70
        }
      ],
      "color": "#dc2626",
      "fillColor": "#ff6b6b"
    },
    {
      "id": "triangle-2",
      "name": "Blue Goal",
      "vertices": [
        {
          "x": 6,
          "y": 119
        },
        {
          "x": 25,
          "y": 144
        },
        {
          "x": 0,
          "y": 144
        },
        {
          "x": 0,
          "y": 70
        },
        {
          "x": 7,
          "y": 70
        }
      ],
      "color": "#2563eb",
      "fillColor": "#60a5fa"
    }
  ],
  "sequence": [
    {
      "kind": "path",
      "lineId": "line-vnaphgu7z2"
    },
    {
      "kind": "path",
      "lineId": "mlh6ajzh-c3xq0z"
    },
    {
      "kind": "path",
      "lineId": "mlh6e6ox-lu5dm7"
    },
    {
      "kind": "path",
      "lineId": "mlh6ey4q-06jtu0"
    },
    {
      "kind": "path",
      "lineId": "mlh6gx44-f36tq9"
    },
    {
      "kind": "path",
      "lineId": "mlh6hos1-5x678j"
    },
    {
      "kind": "path",
      "lineId": "mlh6jilz-hkv7un"
    },
    {
      "kind": "path",
      "lineId": "mlh6lrfb-413cc4"
    }
  ],
  "version": "1.2.1",
  "timestamp": "2026-02-10T22:40:47.516Z"
}