from pathlib import Path


PAGE_W = 595.28
PAGE_H = 841.89
MARGIN = 34


class PdfBuilder:
    def __init__(self):
        self.objects = []

    def add_object(self, data: bytes) -> int:
        self.objects.append(data)
        return len(self.objects)

    def build(self, root_id: int) -> bytes:
        parts = [b"%PDF-1.4\n%\xe2\xe3\xcf\xd3\n"]
        offsets = [0]
        cursor = len(parts[0])
        for index, obj in enumerate(self.objects, start=1):
            offsets.append(cursor)
            block = f"{index} 0 obj\n".encode("ascii") + obj + b"\nendobj\n"
            parts.append(block)
            cursor += len(block)

        xref_start = cursor
        xref = [f"xref\n0 {len(self.objects) + 1}\n".encode("ascii")]
        xref.append(b"0000000000 65535 f \n")
        for off in offsets[1:]:
            xref.append(f"{off:010d} 00000 n \n".encode("ascii"))
        trailer = (
            f"trailer\n<< /Size {len(self.objects) + 1} /Root {root_id} 0 R >>\n"
            f"startxref\n{xref_start}\n%%EOF\n"
        ).encode("ascii")
        return b"".join(parts + xref + [trailer])


def esc(text: str) -> str:
    return text.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)")


class Canvas:
    def __init__(self):
        self.lines = []

    def raw(self, text: str):
        self.lines.append(text)

    def rect(self, x: float, y: float, w: float, h: float, stroke="0.85 0.9 0.96", fill=None, width=1):
        self.lines.append(f"{width} w")
        self.lines.append(f"{stroke} RG")
        if fill:
            self.lines.append(f"{fill} rg")
            self.lines.append(f"{x:.2f} {y:.2f} {w:.2f} {h:.2f} re B")
        else:
            self.lines.append(f"{x:.2f} {y:.2f} {w:.2f} {h:.2f} re S")

    def line(self, x1: float, y1: float, x2: float, y2: float, stroke="0.85 0.9 0.96", width=1):
        self.lines.append(f"{width} w")
        self.lines.append(f"{stroke} RG")
        self.lines.append(f"{x1:.2f} {y1:.2f} m {x2:.2f} {y2:.2f} l S")

    def text(self, x: float, y: float, text: str, font="F1", size=10, color="0.09 0.13 0.20"):
        safe = esc(text)
        self.lines.append("BT")
        self.lines.append(f"/{font} {size} Tf")
        self.lines.append(f"{color} rg")
        self.lines.append(f"1 0 0 1 {x:.2f} {y:.2f} Tm")
        self.lines.append(f"({safe}) Tj")
        self.lines.append("ET")

    def finalize(self) -> bytes:
        return "\n".join(self.lines).encode("ascii")


def wrap(text: str, max_chars: int):
    words = text.split()
    lines = []
    cur = ""
    for word in words:
      candidate = word if not cur else cur + " " + word
      if len(candidate) <= max_chars:
          cur = candidate
      else:
          if cur:
              lines.append(cur)
          cur = word
    if cur:
        lines.append(cur)
    return lines


def draw_wrapped(canvas: Canvas, x: float, y: float, text: str, max_chars: int, size=10, leading=12, font="F1", color="0.25 0.33 0.46"):
    current_y = y
    for line in wrap(text, max_chars):
        canvas.text(x, current_y, line, font=font, size=size, color=color)
        current_y -= leading
    return current_y


def draw_bullets(canvas: Canvas, x: float, y: float, items, max_chars: int, size=9.6, leading=11.2):
    current_y = y
    for item in items:
        lines = wrap(item, max_chars)
        canvas.text(x, current_y, "-", font="F1", size=size, color="0.11 0.17 0.26")
        text_x = x + 10
        for idx, line in enumerate(lines):
            canvas.text(text_x, current_y - (idx * leading), line, font="F1", size=size, color="0.11 0.17 0.26")
        current_y -= leading * max(1, len(lines)) + 1
    return current_y


def draw_steps(canvas: Canvas, x: float, y: float, items, max_chars: int, size=9.6, leading=11.2):
    current_y = y
    for idx, item in enumerate(items, start=1):
        lines = wrap(item, max_chars)
        canvas.text(x, current_y, f"{idx}.", font="F1", size=size, color="0.11 0.17 0.26")
        text_x = x + 12
        for line_index, line in enumerate(lines):
            canvas.text(text_x, current_y - (line_index * leading), line, font="F1", size=size, color="0.11 0.17 0.26")
        current_y -= leading * max(1, len(lines)) + 1
    return current_y


def main():
    out_dir = Path("output/pdf")
    out_dir.mkdir(parents=True, exist_ok=True)
    pdf_path = out_dir / "one-click-video-generator-summary.pdf"

    c = Canvas()

    hero_x = MARGIN
    hero_y = PAGE_H - MARGIN - 108
    hero_w = PAGE_W - (MARGIN * 2)
    hero_h = 108
    c.rect(hero_x, hero_y, hero_w, hero_h, stroke="0.84 0.89 0.96", fill="0.97 0.98 1.00", width=1)
    c.text(hero_x + 16, hero_y + hero_h - 18, "REPO SUMMARY", font="F2", size=9, color="0.11 0.44 0.84")
    c.text(hero_x + 16, hero_y + hero_h - 42, "One Click Video Generator", font="F2", size=22, color="0.09 0.13 0.20")
    hero_text = (
        "A Spring Boot web app that serves a single-page interface for creating riding-themed video files "
        "with one click. Repo evidence shows it generates 30 image frames in Java, attempts FFmpeg-based "
        "MP4 encoding, and downloads the result from the browser."
    )
    draw_wrapped(c, hero_x + 16, hero_y + hero_h - 62, hero_text, 96, size=10.6, leading=13)

    col_gap = 12
    left_w = (hero_w - col_gap) * 0.53
    right_w = hero_w - col_gap - left_w
    top_y = hero_y - 12

    who_h = 88
    run_h = 88
    what_h = 238
    arch_h = 238

    left_x = MARGIN
    right_x = MARGIN + left_w + col_gap

    c.rect(left_x, top_y - who_h, left_w, who_h, stroke="0.84 0.89 0.96", fill="0.96 0.98 0.99")
    c.text(left_x + 12, top_y - 16, "WHO IT'S FOR", font="F2", size=10, color="0.11 0.44 0.84")
    who_text = (
        'Primary persona: a content creator for the "Riding Roney" channel who wants quick, kid-friendly, '
        "safety-focused riding videos without a multi-step editing workflow."
    )
    draw_wrapped(c, left_x + 12, top_y - 34, who_text, 50, size=9.8, leading=12)

    c.rect(right_x, top_y - run_h, right_w, run_h, stroke="0.84 0.89 0.96")
    c.text(right_x + 12, top_y - 16, "HOW TO RUN", font="F2", size=10, color="0.11 0.44 0.84")
    draw_steps(
        c,
        right_x + 12,
        top_y - 34,
        [
            "Install Java 17+ and Maven 3.6+.",
            "From the repo root, run mvn spring-boot:run.",
            "Open http://localhost:8080.",
            "Click CREATE VIDEO to trigger download.",
        ],
        33,
    )

    lower_top = top_y - who_h - 12
    c.rect(left_x, lower_top - what_h, left_w, what_h, stroke="0.84 0.89 0.96")
    c.text(left_x + 12, lower_top - 16, "WHAT IT DOES", font="F2", size=10, color="0.11 0.44 0.84")
    draw_bullets(
        c,
        left_x + 12,
        lower_top - 34,
        [
            "Serves a single Thymeleaf landing page with a prominent create button.",
            "Generates a random riding title and story from in-code preset lists.",
            "Creates 30 frames at 1920x1080 using Java2D text and gradient graphics.",
            "Writes frame PNGs to a per-video frames directory.",
            "Attempts FFmpeg encoding to produce an MP4 download.",
            "Falls back to writing a basic MP4 structure if FFmpeg fails or is missing.",
            "Stores output under ./generated-videos/ and exposes a /health endpoint.",
        ],
        48,
    )

    c.rect(right_x, lower_top - arch_h, right_w, arch_h, stroke="0.84 0.89 0.96", fill="0.96 0.98 0.99")
    c.text(right_x + 12, lower_top - 16, "ARCHITECTURE OVERVIEW", font="F2", size=10, color="0.11 0.44 0.84")

    blocks = [
        ("UI", "templates/index.html renders the page and submits POST /create."),
        ("Web layer", "VideoController serves /, handles /create, and streams the generated file."),
        ("Generation service", "VideoService picks random content, builds frames, saves PNGs, and manages output folders."),
        ("Encoding path", "The service launches an external ffmpeg process; if unavailable, it uses an in-process fallback writer."),
        ("Config", "application.yml sets port, Thymeleaf options, logging, and app video/content settings."),
        ("Data / integrations", "Persistent store: Not found in repo. External API integration: Not found in repo."),
    ]

    bx = right_x + 12
    by = lower_top - 34
    bw = (right_w - 28) / 2
    bh = 58
    for idx, (title, body) in enumerate(blocks):
        col = idx % 2
        row = idx // 2
        x = bx + col * (bw + 8)
        y = by - row * (bh + 8)
        c.rect(x, y - bh + 2, bw, bh, stroke="0.84 0.91 1.00", fill="0.92 0.96 1.00")
        c.text(x + 8, y - 12, title, font="F2", size=9.2, color="0.12 0.21 0.37")
        draw_wrapped(c, x + 8, y - 25, body, 24, size=8.5, leading=10, color="0.19 0.26 0.37")

    footer_y = lower_top - arch_h - 20
    footer_h = 44
    c.rect(MARGIN, footer_y - footer_h, hero_w, footer_h, stroke="0.84 0.89 0.96")
    footer_text = (
        "Evidence basis: README.md, pom.xml, application.yml, index.html, VideoController.java, and VideoService.java. "
        'Items not evidenced by code were omitted; missing details are labeled "Not found in repo."'
    )
    draw_wrapped(c, MARGIN + 12, footer_y - 18, footer_text, 110, size=8.8, leading=10.5, color="0.30 0.37 0.48")

    content = c.finalize()
    builder = PdfBuilder()
    font1 = builder.add_object(b"<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>")
    font2 = builder.add_object(b"<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>")
    content_obj = builder.add_object(
        f"<< /Length {len(content)} >>\nstream\n".encode("ascii") + content + b"\nendstream"
    )
    page_obj = builder.add_object(
        (
            "<< /Type /Page /Parent 5 0 R /MediaBox [0 0 595.28 841.89] "
            f"/Resources << /Font << /F1 {font1} 0 R /F2 {font2} 0 R >> >> "
            f"/Contents {content_obj} 0 R >>"
        ).encode("ascii")
    )
    pages_obj = builder.add_object(b"<< /Type /Pages /Count 1 /Kids [4 0 R] >>")
    catalog_obj = builder.add_object(b"<< /Type /Catalog /Pages 5 0 R >>")

    pdf_path.write_bytes(builder.build(catalog_obj))
    print(pdf_path.resolve())


if __name__ == "__main__":
    main()
