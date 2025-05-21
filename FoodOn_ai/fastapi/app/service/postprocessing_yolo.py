from collections import defaultdict

def postprocess(results, img_width, img_height):
    df = results.pandas().xyxy[0]  # [xmin, ymin, xmax, ymax, confidence, class, name]
    df = df[df['confidence'] >= 0.25]

    grouped = defaultdict(lambda: {"count": 0, "positions": []})

    for _, row in df.iterrows():
        xmin = row["xmin"] / img_width
        ymin = row["ymin"] / img_height
        width = (row["xmax"] - row["xmin"]) / img_width
        height = (row["ymax"] - row["ymin"]) / img_height

        grouped[row['name']]['count'] += 1
        grouped[row['name']]['positions'].append({
            "x": xmin,
            "y": ymin,
            "width": width,
            "height": height,
            "confidence": float(row["confidence"])
        })

    return [{"name": name, "count": data["count"], "positions": data["positions"]} for name, data in grouped.items()]
