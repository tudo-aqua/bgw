import { toJpeg, toPng } from "html-to-image";

const sceneElement = () => document.getElementById("boardGameScene");

/** Resolves once the Kotlin renderer stopped touching the DOM, i.e. the last preview message is on screen. */
export function waitForRender(minimum = 250, quiet = 150, maximum = 2500) {
  const root = document.getElementById("bgw-root");
  return new Promise<void>(resolve => {
    const started = performance.now();
    let last = started;
    const observer = new MutationObserver(() => { last = performance.now(); });
    if (root) observer.observe(root, { attributes: true, childList: true, subtree: true, characterData: true });
    const tick = () => {
      const now = performance.now();
      if ((now - started >= minimum && now - last >= quiet) || now - started >= maximum) {
        observer.disconnect();
        resolve();
      } else setTimeout(tick, 50);
    };
    tick();
  });
}

/** Renders the scene that is currently shown in the preview to an image that is `width` pixels wide. */
export async function captureScene(width: number, format: "png" | "jpeg" = "png"): Promise<string> {
  const element = sceneElement();
  if (!element || element.offsetWidth === 0) throw new Error("The preview is not ready yet.");
  const options = {
    pixelRatio: width / element.offsetWidth,
    // Skipping web font embedding avoids cross-origin stylesheet fetches; scene fonts fall back to installed fonts.
    fontEmbedCSS: "",
    quality: 0.9,
    // The scene is centered inside the preview by absolute offsets; drop them so the image starts at the scene's corner.
    style: { top: "0px", left: "0px", right: "auto", bottom: "auto", margin: "0px" },
    width: element.offsetWidth,
    height: element.offsetHeight,
  };
  return format === "png" ? toPng(element, options) : toJpeg(element, options);
}

export function downloadUrl(url: string, filename: string) {
  const anchor = document.createElement("a");
  anchor.href = url;
  anchor.download = filename;
  anchor.click();
}

export function downloadText(text: string, filename: string, type: string) {
  const url = URL.createObjectURL(new Blob([text], { type }));
  downloadUrl(url, filename);
  URL.revokeObjectURL(url);
}
