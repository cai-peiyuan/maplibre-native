#include <mbgl/renderer/layer_tweaker.hpp>

#include <mbgl/map/transform_state.hpp>
#include <mbgl/style/layer_properties.hpp>
#include <mbgl/renderer/render_tree.hpp>
#include <mbgl/renderer/render_tile.hpp>
#include <mbgl/shaders/layer_ubo.hpp>
#include <mbgl/util/mat4.hpp>

#if MLN_RENDER_BACKEND_METAL
#include <mbgl/shaders/layer_ubo.hpp>
#include <mbgl/util/monotonic_timer.hpp>
#include <chrono>
#endif // MLN_RENDER_BACKEND_METAL

namespace mbgl {

LayerTweaker::LayerTweaker(std::string id_, Immutable<style::LayerProperties> properties)
    : id(std::move(id_)),
      evaluatedProperties(std::move(properties)) {}

bool LayerTweaker::checkTweakDrawable(const gfx::Drawable& drawable) const {
    // Apply to a drawable if it references us, or if doesn't reference anything.
    const auto& tweaker = drawable.getLayerTweaker();
    return !tweaker || tweaker.get() == this;
}

mat4 LayerTweaker::getTileMatrix(const UnwrappedTileID& tileID,
                                 const RenderTree& renderTree,
                                 const TransformState& state,
                                 const std::array<float, 2>& translation,
                                 style::TranslateAnchorType anchor,
                                 bool nearClipped,
                                 bool inViewportPixelUnits,
                                 bool aligned) {
    // from RenderTile::prepare
    mat4 tileMatrix;
    state.matrixFor(/*out*/ tileMatrix, tileID);

    const auto& transformParams = renderTree.getParameters().transformParams;
    // nearClippedMatrix has near plane moved further, to enhance depth buffer precision
    const auto& projMatrix = aligned
                                 ? transformParams.alignedProjMatrix
                                 : (nearClipped ? transformParams.nearClippedProjMatrix : transformParams.projMatrix);
    matrix::multiply(tileMatrix, projMatrix, tileMatrix);

    return RenderTile::translateVtxMatrix(tileID, tileMatrix, translation, anchor, state, inViewportPixelUnits);
}

void LayerTweaker::updateProperties(Immutable<style::LayerProperties> newProps) {
    evaluatedProperties = std::move(newProps);
    propertiesUpdated = true;
}

#if MLN_RENDER_BACKEND_METAL
shaders::ExpressionInputsUBO LayerTweaker::buildExpressionUBO(double zoom, uint64_t frameCount) {
    const auto time = util::MonotonicTimer::now();
    const auto time_ns = std::chrono::duration_cast<std::chrono::nanoseconds>(time).count();
    return {/* .time_lo = */ static_cast<uint32_t>(time_ns),
            /* .time_hi = */ static_cast<uint32_t>(time_ns >> 32),
            /* .frame_lo = */ static_cast<uint32_t>(frameCount),
            /* .frame_hi = */ static_cast<uint32_t>(frameCount >> 32),
            /* .zoom = */ static_cast<float>(zoom),
            /* .zoom_frac = */ static_cast<float>(zoom - static_cast<float>(zoom)),
            /* .pad = */ 0,
            0};
}

bool LayerTweaker::hasPropertyAsUniform(const StringIdentity attrNameID) const {
    return propertiesAsUniforms.find(attrNameID) != propertiesAsUniforms.end();
}

using namespace shaders;
AttributeSource LayerTweaker::getAttributeSource(const StringIdentity attribNameID) const {
    return hasPropertyAsUniform(attribNameID) ? AttributeSource::Constant : AttributeSource::PerVertex;
}
#endif // MLN_RENDER_BACKEND_METAL

void LayerTweaker::setPropertiesAsUniforms([[maybe_unused]] const std::unordered_set<StringIdentity>& props) {
#if MLN_RENDER_BACKEND_METAL
    if (props != propertiesAsUniforms) {
        propertiesAsUniforms = props;
        permutationUpdated = true;
    }
#endif
}

#if !MLN_RENDER_BACKEND_METAL
namespace {
const std::unordered_set<StringIdentity> emptyIDSet;
}
#endif

const std::unordered_set<StringIdentity>& LayerTweaker::getPropertiesAsUniforms() const {
#if MLN_RENDER_BACKEND_METAL
    return propertiesAsUniforms;
#else
    return emptyIDSet;
#endif
}

void LayerTweaker::enableOverdrawInspector(bool value) {
    if (overdrawInspector != value) {
        overdrawInspector = value;
        permutationUpdated = true;
    }
}

} // namespace mbgl
