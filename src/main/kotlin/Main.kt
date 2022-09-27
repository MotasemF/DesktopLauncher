// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache width = 2.dp.0 license that can be found in the LICENSE file.
import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.*
import java.io.File

@Composable
@Preview
fun App(window: ComposeWindow) {
    val boxBackgroundColor = Color(0XFFF7F4F3)
    val borderColor = Color(0XFFE7E5E4)
    val iconColor = Color(0XFF1462DC)

    var isDraggableImage by remember { mutableStateOf(false) }
    val iconDragColorAnim = animateColorAsState(if (isDraggableImage) iconColor.copy(0.5f) else boxBackgroundColor)
    var isDraggableExc by remember { mutableStateOf(false) }
    var excDragColorAnim = animateColorAsState(if (isDraggableExc) iconColor.copy(0.5f) else boxBackgroundColor)
    var launcherTitle by remember { mutableStateOf("") }

    var iconPath by remember { mutableStateOf("") }
    var execPath by remember { mutableStateOf("") }

    window.dropTarget = object : DropTarget() {
        override fun drop(dtde: DropTargetDropEvent) {
            dtde.acceptDrop(DnDConstants.ACTION_COPY)
            val droppedFiles = dtde.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
            if (droppedFiles.isNotEmpty() && droppedFiles.size <= 1) {
                val file = droppedFiles.first()
                if (FileUtils.isImage(file)) {
                    iconPath = file.absolutePath
                } else {
                    execPath = file.absolutePath
                }
            }
        }

        override fun dragEnter(dtde: DropTargetDragEvent) {
            val droppedFiles = dtde.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>
            println(droppedFiles)
            if (droppedFiles.isNotEmpty() && droppedFiles.size <= 1) {
                val file = droppedFiles.first()
                if (FileUtils.isImage(file)) {
                    isDraggableImage = true
                } else {
                    if (file.canExecute()) {
                        isDraggableExc = true
                    }
                }
            }
        }

        override fun dragExit(dte: DropTargetEvent?) {
            super.dragExit(dte)
            isDraggableImage = false
            isDraggableExc = false
        }
    }

    MaterialTheme {
        Column {
            Spacer(Modifier.size(16.dp))
            BasicTextField(
                value = launcherTitle,
                onValueChange = { launcherTitle = it },
                textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 12.sp),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                cursorBrush = Brush.linearGradient(listOf(Color.Red, Color.Gray)),

                ) {
                Box(
                    Modifier.height(45.dp).fillMaxWidth().padding(horizontal = 32.dp)
                        .background(boxBackgroundColor, RoundedCornerShape(10.dp)).dashedBorder(2.dp, 5.dp, borderColor)
                ) {
                    Box(Modifier.align(Alignment.CenterStart).padding(horizontal = 16.dp)) {
                        it()
                        if (launcherTitle.isBlank()) {
                            Text(
                                text = "Launcher name",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.size(16.dp))
            Box(
                Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 32.dp)
                    .background(iconDragColorAnim.value, RoundedCornerShape(10.dp)).align(Alignment.CenterHorizontally)
                    .dashedBorder(width = 2.dp, 5.dp, iconDragColorAnim.value)
            ) {
                Column(Modifier.align(Alignment.Center)) {
                    Image(
                        painter = painterResource("file.svg"),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(iconColor),
                        modifier = Modifier.size(40.dp).align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "Drag-n-Drop logo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(Modifier.size(16.dp))
            Box(
                Modifier.fillMaxWidth().height(200.dp).padding(horizontal = 32.dp)
                    .background(excDragColorAnim.value, RoundedCornerShape(10.dp)).align(Alignment.CenterHorizontally)
                    .dashedBorder(width = 2.dp, 5.dp, excDragColorAnim.value)
            ) {
                Column(Modifier.align(Alignment.Center)) {
                    Image(
                        painter = painterResource("file.svg"),
                        contentDescription = "",
                        colorFilter = ColorFilter.tint(iconColor),
                        modifier = Modifier.size(40.dp).align(Alignment.CenterHorizontally)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "Drag-n-Drop excutable",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(Modifier.size(16.dp))
            Row {
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = {
                        isDraggableImage = false
                        isDraggableExc = false
                        launcherTitle = ""
                        iconPath = ""
                        execPath = ""
                    },
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = Color(0XFFD16666),
                        contentColor = Color.White
                    )
                ) {
                    Text("New")
                }
                Spacer(Modifier.size(8.dp))
                Button(modifier = Modifier
                    .padding(end = 32.dp),
                    colors = androidx.compose.material.ButtonDefaults.buttonColors(
                        backgroundColor = iconColor
                    ),
                    onClick = {
                        if (launcherTitle.isNotEmpty() && iconPath.isNotEmpty() && execPath.isNotEmpty()) {
                            FileUtils.createLauncherFile(launcherTitle, iconPath, execPath)
                        }

                    }) {
                    Text(text = "Make Launcher", color = Color.White)
                }
            }
        }
    }
}

fun main() = application {
    val windowState = rememberWindowState(width = 500.dp, height = 600.dp)
    Window(onCloseRequest = ::exitApplication, title = "Create Desktop Launcher", resizable = false, state = windowState, icon = painterResource("logo.png")) {
        App(window)
    }
}
