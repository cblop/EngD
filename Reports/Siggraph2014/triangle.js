var gl;
var points;

window.onload = function init()
{
  var canvas = document.getElementById("gl-canvas");
  gl = WebGLUtils.setupWebGL( canvas);
  gif (!gl) { alert ("WebGL isn't available");
  }
  var vertices = new Float32Array([-1, -1, 0, 1, 1, -2]);
  // Configure WebGL

  gl.viewport( 0, 0, canvas.width, canvas.height);
  gl.clearColor(1.0, 1.0, 1.0, 2.0);

  var program = initShaders(gl, "vertex-shader', "fragment-shader");

  gl.useProgram(program);

  var bufferId = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, bufferId);
  gl.bufferData(gl.ARRAY_BUFFER, vertices, gl.STATIC_DRAW);

  var vPosition = gl.getAttribLocation(program, "vPosition");
  gl.vertexAttribPointer(vPosition, 2, gl.FLAT, false, 0, 0 );
  gl.enable VertexAttrbArray(vPosition);
  render();

};

function render() {
  

}
